import logo_v from '@/assets/images/logo_v.webp';
import signup_img from '@/assets/images/signup.svg';
import AuthService from '@Services/auth';
import ToolUsb from '@Services/toolUsb';
import { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useHistory, Link } from 'react-router-dom';
import GeneralService from '@Services/general';
import { IconCalendar2 } from '@Components/Icon';
import { useDispatch } from 'react-redux';
import { setUser } from '@Redux/Actions/Auth';
import { remove, get } from "@Utils/cookie";
import { setInstallToolModal } from '@Redux/Actions/InstallToolModal';
import { addToast } from '@Redux/Actions/AlertToast';
import SimpleBar from 'simplebar-react';
import $ from 'jquery';
function PageSignupProfile() {
  const history = useHistory();
  const {
    watch,
    register,
    getValues,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm();
  const dispatch = useDispatch();

  const [isRequesting, setIsRequesting] = useState(false);
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const watchProvince = watch('proviceCode', null);
  const watchDistrict = watch('districtCode', null);
  const watchWard = watch('wardCode', null);

  async function onSubmit(data) {
    data.phoneNumber = data.phoneNumber.trim();
    data.idNo = data.idNo.trim();
    try {
      setIsRequesting(true);
      const companyData = await getCompanyData();
      const resProfile = await AuthService.updateProfile(data);
      const resCompany = await AuthService.addCompany(companyData);
      const user = { ...resProfile.data, ...{ company: resCompany.data } };
      dispatch(setUser(user));
      history.push('/trang-chu');
    } catch (err) {
      switch (err) {
        case 'NOT_TOOL':
          dispatch(setInstallToolModal({ isShow: true }));
          break;
        case 'NOT_USBTOKEN':
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ATTACH_USBTOKEN_SIGNUP' }));
          break;
        default:
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red", message: err.errorMsg }));
          break;
      }
    } finally {
      setIsRequesting(false);
    }
  }

  function getCompanyData() {
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          const resToken = await ToolUsb.getToken();
          const checkUSB = await ToolUsb.getCheckUsb(resToken.data);
          if (checkUSB.message.toUpperCase() == 'DISCONNECTED') {
            reject('NOT_USBTOKEN');
          } else {
            const inforUSB = await ToolUsb.getCertificate(resToken.data);
            const data = {
              name: inforUSB.data.company,
              email: inforUSB.data.email,
              foundDate: '',
              phoneNumber: '',
              taxCode: inforUSB.data.taxCode,
              address: inforUSB.data.location ? inforUSB.data.location : ''
            };
            resolve(data);
          }
        } catch (err) {
          reject('NOT_TOOL');
        }
      })();
    });
  }
  const getAge = (birthDate) => {
    var today = new Date();
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  }
  const isInvalidBirthday = () => {
    const val = getValues();
    if (val.birthDate) {
      return getAge(new Date(tranformDate(val.birthDate))) >= 18;
    }
    return false;
  }
  function tranformDate(date) {
    const arr = date.split('/');
    return `${arr[1]}/${arr[0]}/${arr[2]}`;
  }
  const isInValidFounded = () => {
    const val = getValues();
    if (val.issuedOn && val.birthDate) {
      return new Date(tranformDate(val.issuedOn)).getTime() > new Date(tranformDate(val.birthDate)).getTime();
    }
    return false;
  };
  // function validateBirthday(e) {
  //   const birthdayYear = new Date(`${e}`).getFullYear();
  //   const birthdayMonth = new Date(`${e}`).getMonth();
  //   const birthdayDate = new Date(`${e}`).getDate();
  //   const currentYear = new Date().getFullYear();
  //   const currentMonth = new Date().getMonth();
  //   const currentDate = new Date().getDate();
  //   let month = currentMonth - birthdayMonth;
  //   let age = currentYear - birthdayYear;
  //   if (month < 0 || (month === 0 && currentDate < birthdayDate)) {
  //     age--;
  //   }
  //   if (age < 18) {
  //     return false;
  //   } else {
  //     return true;
  //   }
  // }

  function handleProvince(val) {
    if (val) {
      const arr = provinces.filter(item => item.name.toUpperCase() == val.toUpperCase());
      GeneralService.getDistricts(arr[0].id)
        .then(res => {
          setDistricts(res.data);
          setValue('districtCode', '');
          setValue('wardCode', '');
        })
        .catch(err => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red', message: err.errorMsg }));
        });
    }
  }

  function handleDistrict(val) {
    if (val && val != watchDistrict) {
      const arr = districts.filter(item => `${item.prefix} ${item.name}`.toUpperCase() == val.toUpperCase());
      GeneralService.getWards(arr[0].id)
        .then(res => {
          setWards(res.data);
        })
        .catch(err => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red', message: err.errorMsg }));
        });
    }
  }

  function getProvinces() {
    GeneralService.getProvinces()
      .then(res => {
        setProvinces(res.data);
      })
      .catch(err => {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red', message: err.errorMsg }));
      });
  }
  function handleLogout() {
    if (get('digital_signature_token')) {
      AuthService.logout()
        .then((res) => {
          remove('digital_signature_token');
          remove('digital_signature_token_type');
          remove('digital_signature_refresh_token');
          window.location.href = '/dang-nhap';
        })
        .catch((err) => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red", message: err.errorMsg }));
        });
    } else {
      window.location.href = '/dang-nhap';
    }
  }
  window.addEventListener('popstate', function (e) {
    if (get('digital_signature_token')) {
      AuthService.logout()
        .then((res) => {
          remove('digital_signature_token');
          remove('digital_signature_token_type');
          remove('digital_signature_refresh_token');
          window.location.href = '/dang-nhap';
        })
        .catch((err) => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red", message: err.errorMsg }));
        });
    } else {
      window.location.href = '/dang-nhap';
    }
  });
  useEffect(() => {
    getProvinces();
  }, []);

  useEffect(() => {
    document.title = 'MContract';
    $('.datepicker-date').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true,
      weekStart: 1,
      language: "vi",
      todayHighlight: true,
      startDate: "01/01/1900",
    }).on('hide', function () {
      if (!this.firstHide) {
        if (!$(this).is(":focus")) {
          this.firstHide = true;
          this.focus();
        }
      } else {
        this.firstHide = false;
      }
    }).on('show', function () {
      if (this.firstHide) {
        $(this).datepicker('hide');
      }
    });
  }, []);

  return (
    < SimpleBar className="container-fluid main_wrapper" style={{ background: '#FFF', minHeight: '100vh', maxHeight: '100vh' }}>
      <div className="row">
        <div className="col-12 col-lg pl-0 d-none d-lg-block">
          <div className="site_left">
            <div className="site_left_wrapper">
              <div className="site_left_content">
                <div className="slbtn_area">
                  <h4>Bạn đã có tài khoản?</h4>
                  <Link onClick={handleLogout} className="btn btn_sec2">
                    Đăng nhập
                  </Link>
                </div>
                <img className="img-fluid" src={signup_img} alt="" loading="lazy" />
              </div>
            </div>
          </div>
        </div>
        <div className="col-12 col-lg site_right">
          <div className="site_form info_form">
            <div className="text-center">
              <a href="javascript:void(0)" title="MContract ">
                <img className="site_logo img-fluid" alt="" src={logo_v} loading="lazy" />
              </a>
            </div>
            <h1 className="site_title">Thông tin tài khoản</h1>
            <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
              {/* <input type="text" autoComplete="new-passowrd" style={{ display: 'none' }} /> */}
              <div className="row">
                <div className="col-12 col-sm">
                  <div className="form-group p-0">
                    <label htmlFor="fullname" className="mb-0">
                      Họ tên
                    </label>
                    <input type="text" className="form-control" id="fullname" autoComplete="disable" {...register('fullName',
                      { required: true, maxLength: 100, pattern: /^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/ })} />
                    <span className="input_icon">
                      <svg xmlns="http://www.w3.org/2000/svg" height="22" viewBox="0 0 24 24" width="22" fill="currentColor">
                        <path d="M0 0h24v24H0V0z" fill="none" />
                        <path d="M12 5.9c1.16 0 2.1.94 2.1 2.1s-.94 2.1-2.1 2.1S9.9 9.16 9.9 8s.94-2.1 2.1-2.1m0 9c2.97 0 6.1 1.46 6.1 2.1v1.1H5.9V17c0-.64 3.13-2.1 6.1-2.1M12 4C9.79 4 8 5.79 8 8s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 9c-2.67 0-8 1.34-8 4v2c0 .55.45 1 1 1h14c.55 0 1-.45 1-1v-2c0-2.66-5.33-4-8-4z" />
                      </svg>
                    </span>
                    {errors.fullName && errors.fullName?.type === 'required' && <span className="message-errors">Mời nhập họ tên</span>}
                    {errors.fullName && errors.fullName?.type === 'pattern' && <span className="message-errors">Họ tên không hợp lệ</span>}
                    {errors.fullName && errors.fullName?.type === 'maxLength' && <span className="message-errors">Họ tên không vượt quá 100 ký tự.</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="birthday" className="mb-0">
                      Ngày sinh
                    </label>
                    <input
                      type="text"
                      className="form-control datepicker-date"
                      id="birthday"
                      autoComplete="disable"
                      {...register('birthDate', {
                        required: true,
                        validate: isInvalidBirthday
                        // validate: {
                        //   checkBirthday: e => validateBirthday(e),
                        // },
                      })}
                    />
                    <span className="input_icon">
                      <IconCalendar2 />
                    </span>
                    {errors.birthDate && errors.birthDate?.type === 'required' && <span className="message-errors">Mời nhập ngày sinh</span>}
                    {errors.birthDate && errors.birthDate?.type === 'validate' && <span className="message-errors">Bạn phải đủ 18 tuổi</span>}
                  </div>
                  <div className="form-group p-0">
                    <label className="mb-0">Giới tính</label>
                    <div className="gender_area">
                      <div className="custom-control custom-radio custom-control-inline mr-4">
                        <input type="radio" id="gender1" name="gender" className="custom-control-input" value="Nam" {...register('sex', { required: true })} />
                        <label className="custom-control-label label-signup" htmlFor="gender1">
                          Nam
                        </label>
                      </div>
                      &nbsp;
                      <div className="custom-control custom-radio custom-control-inline">
                        <input type="radio" id="gender2" name="gender" className="custom-control-input" value="Nữ" {...register('sex', { required: true })} />
                        <label className="custom-control-label label-signup" htmlFor="gender2">
                          Nữ
                        </label>
                      </div>
                      <br />
                      {errors.sex && errors.sex?.type === 'required' && <span className="message-errors">Mời chọn giới tính</span>}
                    </div>
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="identity" className="mb-0">
                      Số CMND/CCCD
                    </label>
                    <input type="text" className="form-control" id="identity" autoComplete="disable" {...register('idNo', { required: true, pattern: /^([\s]{0,})([0-9]{9}|[0-9]{12})([\s]{0,})$/g })} />
                    <span className="input_icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-card-heading" viewBox="0 0 16 16">
                        <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h13zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-13z" />
                        <path d="M3 8.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5zm0-5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-1z" />
                      </svg>
                    </span>
                    {errors.idNo && errors.idNo?.type === 'required' && <span className="message-errors">Mời nhập CMND/CCCD</span>}
                    {errors.idNo && errors.idNo?.type === 'pattern' && <span className="message-errors">CMND/CCCD không hợp lệ</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="valid_date" className="mb-0">
                      Ngày cấp
                    </label>
                    <input type="text" className="form-control datepicker-date" id="valid_date" autoComplete="disable" {...register('issuedOn', { required: true, validate: isInValidFounded })} />
                    <span className="input_icon">
                      <IconCalendar2 />
                    </span>
                    {errors.issuedOn && errors.issuedOn?.type === 'required' && <span className="message-errors">Mời nhập ngày cấp</span>}
                    {errors.issuedOn && errors.issuedOn?.type === 'validate' && <span className="message-errors">Ngày cấp phải lớn hơn ngày sinh</span>}
                  </div>
                </div>

                <div className="col-12 col-sm">
                  <div className="form-group p-0">
                    <label htmlFor="phone" className="mb-0">
                      Số điện thoại
                    </label>
                    <input type="tel" className="form-control" id="phone" autoComplete="disable" {...register('phoneNumber', { required: true, pattern: /^([\s]{0,})[0][1-9]([0-9]{8}|[0-9]{9})([\s]{0,})$/g })} />
                    <span className="input_icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-telephone" viewBox="0 0 16 16">
                        <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z" />
                      </svg>
                    </span>
                    {errors.phoneNumber && errors.phoneNumber?.type === 'required' && <span className="message-errors">Mời nhập số điện thoại</span>}
                    {errors.phoneNumber && errors.phoneNumber?.type === 'pattern' && <span className="message-errors">Số điện thoại không hợp lệ</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="city" className="mb-0">
                      Tỉnh/Thành phố
                    </label>
                    <select id="city" className="custom-select" {...register('proviceCode', { required: true, setValueAs: e => handleProvince(e) })}>
                      <option value="">Vui lòng chọn</option>
                      {provinces.map((pro, index) => (
                        <option key={index} value={pro.name}>
                          {pro.name}
                        </option>
                      ))}
                    </select>
                    {errors.proviceCode && errors.proviceCode?.type === 'required' && <span className="message-errors">Mời chọn Tỉnh/Thành phố</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="district" className="mb-0">
                      Quận/Huyện
                    </label>
                    <select id="district" disabled={!watchProvince} className="custom-select" {...register('districtCode', { required: true, setValueAs: e => handleDistrict(e) })}>
                      <option value="">Vui lòng chọn</option>
                      {districts.map((district, index) => (
                        <option key={index} value={`${district.prefix} ${district.name}`}>
                          {`${district.prefix} ${district.name}`}
                        </option>
                      ))}
                    </select>
                    {errors.districtCode && errors.districtCode?.type === 'required' && <span className="message-errors">Mời chọn Quận/Huyện</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="ward" className="mb-0">
                      Phường/Xã
                    </label>
                    <select disabled={!watchDistrict} id="ward" className="custom-select" {...register('wardCode', { required: true })}>
                      <option value="">Vui lòng chọn</option>
                      {wards.map((ward, index) => (
                        <option key={index} value={`${ward.prefix} ${ward.name}`}>
                          {`${ward.prefix} ${ward.name}`}
                        </option>
                      ))}
                    </select>
                    {errors.wardCode && errors.wardCode?.type === 'required' && <span className="message-errors">Mời chọn Phường/Xã</span>}
                  </div>
                  <div className="form-group p-0">
                    <label htmlFor="address" className="mb-0">
                      Địa chỉ
                    </label>
                    <input type="text" className="form-control" id="address" {...register('address', { required: true, pattern: /^([\s]{0,})([^\s.]+\s?){1,}([\s]{0,})$/g })} autoComplete="off" />
                    <span className="input_icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-geo" viewBox="0 0 16 16">
                        <path
                          fillRule="evenodd"
                          d="M8 1a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM4 4a4 4 0 1 1 4.5 3.969V13.5a.5.5 0 0 1-1 0V7.97A4 4 0 0 1 4 3.999zm2.493 8.574a.5.5 0 0 1-.411.575c-.712.118-1.28.295-1.655.493a1.319 1.319 0 0 0-.37.265.301.301 0 0 0-.057.09V14l.002.008a.147.147 0 0 0 .016.033.617.617 0 0 0 .145.15c.165.13.435.27.813.395.751.25 1.82.414 3.024.414s2.273-.163 3.024-.414c.378-.126.648-.265.813-.395a.619.619 0 0 0 .146-.15.148.148 0 0 0 .015-.033L12 14v-.004a.301.301 0 0 0-.057-.09 1.318 1.318 0 0 0-.37-.264c-.376-.198-.943-.375-1.655-.493a.5.5 0 1 1 .164-.986c.77.127 1.452.328 1.957.594C12.5 13 13 13.4 13 14c0 .426-.26.752-.544.977-.29.228-.68.413-1.116.558-.878.293-2.059.465-3.34.465-1.281 0-2.462-.172-3.34-.465-.436-.145-.826-.33-1.116-.558C3.26 14.752 3 14.426 3 14c0-.599.5-1 .961-1.243.505-.266 1.187-.467 1.957-.594a.5.5 0 0 1 .575.411z"
                        />
                      </svg>
                    </span>
                    {errors.address && errors.address?.type === 'required' && <span className="message-errors">Mời nhập địa chỉ</span>}
                    {errors.address && errors.address?.type === 'pattern' && <span className="message-errors">Mời nhập địa chỉ</span>}
                  </div>
                </div>
              </div>
              <div className="form_btn text-center mt-4">
                <a onClick={handleLogout} className="btn btn_sec1 back_btn">
                  Quay lại
                </a>
                <button disabled={isRequesting} className="btn btn_pri1 submit_btn">
                  {isRequesting ? (
                    <>
                      <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                    </>
                  ) : (
                    <>Đăng ký</>
                  )}
                </button>
              </div>
            </form>
            <div className="bl_area text-center d-block d-lg-none">
              <p className="mb-0">Bạn đã có tài khoản?</p>
              <a onClick={handleLogout} className="link_site">
                Đăng nhập
              </a>
            </div>
          </div>
        </div>
      </div>
    </SimpleBar>
  );
}

export { PageSignupProfile };
