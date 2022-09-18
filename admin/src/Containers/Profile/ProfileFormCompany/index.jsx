import Select from '@Components/Select';
import { setUser } from '@Redux/Actions/Auth';
import AuthService from '@Services/auth';
import GeneralService from '@Services/general';
import ProfileService from '@Services/profile';
import { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';
import { IconCalendar2 } from '@Components/Icon';
import { addToast } from '@Redux/Actions/AlertToast';
import UploadLogoModal from '@Components/UploadLogoModal';
import { splitSpace } from '@Utils/helpers';
function FormCompany() {
  const user = useSelector(state => state.auth.user);
  const dispatch = useDispatch();
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [image, setImage] = useState("");
  const {
    register,
    setValue,
    handleSubmit,
    getValues,
    formState: { errors },
  } = useForm({
    defaultValues: {
      logoPath: user.company.logoPath,
      name: user.company.name,
      email: user.company.email,
      foundDate: user.company.foundDate,
      phoneNumber: user.company.phoneNumber,
      taxCode: user.company.taxCode,
      // proviceCode: user.company.proviceCode,
      // districtCode: user.company.districtCode,
      // wardCode: user.company.wardCode,
      address: user.company.address,
    },
  });

  // const [provinces, setProvinces] = useState([]);

  // const [districts, setDistricts] = useState([]);
  // const [isLoadingDistrict, setIsLoadingDistrict] = useState(false);

  // const [wards, setWards] = useState([]);
  // const [isLoadingWard, setIsLoadingWard] = useState(false);

  const [isSubmitting, setIsSubmitting] = useState(false);

  const [isUploading, setIsUploading] = useState(false);

  const [acceptEmail, setAcceptEmail] = useState(() => {
    return user.acceptEmailNotification;
  });

  const onSubmitForm = async () => {
    try {
      setIsSubmitting(true);
      // const { name, taxCode, address, proviceCode, districtCode, wardCode } = user.company;
      // const data = {
      //   ...getValues(),
      //   ...{ name, taxCode, address, proviceCode, districtCode, wardCode },
      // };
      const { name, taxCode, address } = user.company;
      const data = {
        ...getValues(),
        ...{ name, taxCode, address },
      };
      const newObj = splitSpace(data, ['logoPath', 'name', 'taxCode', 'address'])
      await ProfileService.updateCompany(newObj);
      const res = await AuthService.profile();
      dispatch(setUser(res.data));
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'SUCCESS_INFOR' }));
    } catch (e) {
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red" }));
    } finally {
      setIsSubmitting(false);
    }
  };

  const onLogoChange = async e => {
    if (e.target.files.length > 0) {
      const file = e.target.files[0];
      setImage(file);
      setShowUploadModal(true);
    }
  }

  const onSubmitLogo = async (file) => {
    if (file) {
      try {
        setIsUploading(true);
        const formData = new FormData();
        formData.append('file', file);
        const res = await GeneralService.uploadFile(formData);
        setValue('logoPath', res.data.path);
        // const { name, email, taxCode, address, proviceCode, districtCode, wardCode } = user.company;
        // const data = {
        //   ...getValues(),
        //   ...{ name, email, taxCode, address, proviceCode, districtCode, wardCode },
        // };
        const { name, email, taxCode, address } = user.company;
        const data = {
          ...getValues(),
          ...{ name, email, taxCode, address },
        };
        await ProfileService.updateCompany(data);
        const resProfile = await AuthService.profile();
        dispatch(setUser(resProfile.data));
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      } finally {
        setIsUploading(false);
      }
    }
  }
  const handleClose = () => {
    setShowUploadModal(false);
    const inputFile = document.getElementById('change-logo-company');
    inputFile.value = '';
  };

  function AcceptEmailNoti(e) {
    AuthService.acceptEmailNoti({ status: e.target.checked })
      .then(res => {
        user.acceptEmailNotification = res.data.acceptEmailNotification;
        setAcceptEmail(res.data.acceptEmailNotification);
      })
      .catch(err => {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      });
  }

  // function onProvinceChange(provinceName) {
  //   if (provinceName) {
  //     const arr = provinces.filter(item => item.name.toUpperCase() == provinceName.toUpperCase());
  //     setIsLoadingDistrict(true);
  //     GeneralService.getDistricts(arr[0].id).then(resDistricts => {
  //       const arrayDistricts = resDistricts.data;
  //       setDistricts(arrayDistricts);
  //       setValue('districtCode', ``);
  //       setValue('wardCode', ``);
  //       setIsLoadingDistrict(false);
  //       setIsLoadingWard(true);
  //     });
  //   }
  // }

  // function onDistrictChange(districtName) {
  //   if (districtName) {
  //     const arr = districts.filter(item => `${item.prefix} ${item.name}`.toUpperCase() == districtName.toUpperCase());
  //     GeneralService.getWards(arr[0].id)
  //       .then(res => {
  //         setIsLoadingWard(false);
  //         const arrayWards = res.data;
  //         setWards(arrayWards);
  //         setValue('wardCode', ``);
  //       })
  //       .catch(err => {
  //         dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
  //       });
  //   }
  // }

  // useEffect(() => {
  //   (async () => {
  //     const provinceRes = await GeneralService.getProvinces();
  //     setProvinces(provinceRes.data);
  //     const arr = provinceRes.data.filter(p => p.name.toUpperCase() == user.company.proviceCode.toUpperCase());
  //     if (arr.length > 0) {
  //       const districtRes = await GeneralService.getDistricts(arr[0].id);
  //       setDistricts(districtRes.data);
  //       setIsLoadingDistrict(false);
  //       const arrDis = districtRes.data.filter(d => `${d.prefix.toUpperCase()} ${d.name.toUpperCase()}` == user.company.districtCode.toUpperCase());
  //       if (arrDis.length > 0) {
  //         const wardRes = await GeneralService.getWards(arrDis[0].id);
  //         setWards(wardRes.data);
  //         setIsLoadingWard(false);
  //       } else {
  //         setIsLoadingWard(true);
  //       }
  //     } else {
  //       setIsLoadingDistrict(true);
  //       setIsLoadingWard(true);
  //     }
  //   })();
  //   // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, []);
  // function onWardChange(wardName) {
  // }
  function tranformDate(date) {
    const arr = date.split('/');
    return `${arr[1]}/${arr[0]}/${arr[2]}`;
  }
  const isInValidFounded = () => {
    const val = getValues();
    if (val.foundDate && user.birthDate) {
      return new Date(tranformDate(val.foundDate)).getTime() > new Date(tranformDate(user.birthDate)).getTime();
    }
    return false;
  };

  return (
    <>
      {Object.keys(user.company).length > 0 && (
        <div className="account_section">
          <div className="as_header">
            <h4>Thông tin doanh nghiệp</h4>
          </div>
          <form onSubmit={handleSubmit(onSubmitForm)} className="profile-company">
            <div className="row">
              <div className="col-12 col-sm-auto d-flex align-items-center">
                <div className="as_img_wrap">
                  <div className="com_img_wrap d-flex justify-content-center align-items-center">
                    {isUploading ? (
                      <>
                        <div className="spinner-upload">
                          <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" />
                        </div>
                      </>
                    ) : (
                      <>
                        <img
                          src={getValues('logoPath') ? getValues('logoPath') : ''}
                          className="rounded-circle company_logo"
                          onError={e => {
                            e.target.style.display = 'none';
                          }}
                          alt=""
                          loading="lazy"
                        />
                        <div className="img_alt">
                          <span>{GeneralService.getLetterName(user.company.name)}</span>
                        </div>
                      </>
                    )}
                  </div>
                  <label htmlFor="change-logo-company" className="change_icon" title="Thay đổi logo">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-image" viewBox="0 0 16 16">
                      <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"></path>
                      <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z"></path>
                    </svg>
                  </label>
                  <input type="file" title="Thay đổi logo" id="change-logo-company" className="change_icon" onChange={e => onLogoChange(e)} accept="image/png, image/jpeg, image/jpg" />
                </div>
              </div>
              <div className="col-12 col-sm">
                <div className="form-group">
                  <label htmlFor="companyName" className="mb-0">
                    Tên công ty
                  </label>
                  <input type="text" className="form-control" id="companyName" {...register('name')} disabled />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-building" viewBox="0 0 16 16">
                      <path
                        fillRule="evenodd"
                        d="M14.763.075A.5.5 0 0 1 15 .5v15a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5V14h-1v1.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V10a.5.5 0 0 1 .342-.474L6 7.64V4.5a.5.5 0 0 1 .276-.447l8-4a.5.5 0 0 1 .487.022zM6 8.694L1 10.36V15h5V8.694zM7 15h2v-1.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 .5.5V15h2V1.309l-7 3.5V15z"
                      />
                      <path d="M2 11h1v1H2v-1zm2 0h1v1H4v-1zm-2 2h1v1H2v-1zm2 0h1v1H4v-1zm4-4h1v1H8V9zm2 0h1v1h-1V9zm-2 2h1v1H8v-1zm2 0h1v1h-1v-1zm2-2h1v1h-1V9zm0 2h1v1h-1v-1zM8 7h1v1H8V7zm2 0h1v1h-1V7zm2 0h1v1h-1V7zM8 5h1v1H8V5zm2 0h1v1h-1V5zm2 0h1v1h-1V5zm0-2h1v1h-1V3z" />
                    </svg>
                  </span>
                  {errors.name && errors.name?.type === 'required' && <span className="message-errors">Mời nhập tên công ty</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="comphone" className="mb-0">
                    Số điện thoại
                  </label>
                  <input type="tel"
                    // className={`form-control ${errors.phoneNumber ? 'is-invalid' : ''} `} 
                    className="form-control"
                    id="comphone" autoComplete="disable" {...register('phoneNumber', { required: true, pattern: /^([\s]{0,})[0][1-9]([0-9]{8})([\s]{0,})$/g })} />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-telephone" viewBox="0 0 16 16">
                      <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z" />
                    </svg>
                  </span>
                  {errors.phoneNumber && errors.phoneNumber?.type === 'required' && <span className="message-errors">Mời nhập số điện thoại</span>}
                  {errors.phoneNumber && errors.phoneNumber?.type === 'pattern' && <span className="message-errors">Số điện thoại không hợp lệ</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="email" className="mb-0">
                    Email
                  </label>
                  <input type="email" className="form-control" id="email" {...register('email', { required: true, pattern: /^([\s]{0,})[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}([\s]{0,})$/g })} />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-envelope" viewBox="0 0 16 16">
                      <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2zm13 2.383l-4.758 2.855L15 11.114v-5.73zm-.034 6.878L9.271 8.82 8 9.583 6.728 8.82l-5.694 3.44A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.739zM1 11.114l4.758-2.876L1 5.383v5.73z" />
                    </svg>
                  </span>
                  {errors.email && errors.email?.type === 'required' && <span className="message-errors d-block">Mời nhập email</span>}
                  {errors.email && errors.email?.type === 'pattern' && <span className="message-errors d-block">Email không hợp lệ</span>}
                </div>

              </div>
              <div className="col-12 col-sm">
                {/* <div className="form-group">
                  <label htmlFor="city" className="mb-0">
                    Tỉnh/Thành phố
                  </label>
                  <select disabled id="city" className="custom-select">
                    {user.company.proviceCode ? <option selected value={user.company.proviceCode}>{user.company.proviceCode}</option> : ""}
                  </select>
                  {errors.proviceCode && errors.proviceCode?.type === 'required' && <span className="message-errors">Mời chọn Tỉnh/Thành phố</span>}
                </div> */}
                {/* <div className="form-group">
                  <label htmlFor="district" className="mb-0">
                    Quận/Huyện
                  </label>
                  <select disabled id="district" className="custom-select">
                    {user.company.districtCode ? <option selected value={user.company.districtCode}>{user.company.districtCode}</option> : ""}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="ward" className="mb-0">
                    Phường/Xã
                  </label>
                  <select disabled id="district" className="custom-select">
                    {user.company.wardCode ? <option selected value={user.company.wardCode}>{user.company.wardCode}</option> : ""}
                  </select>
                </div> */}
                <div className="form-group">
                  <label htmlFor="taxcode" className="mb-0">
                    Mã số thuế
                  </label>
                  <input type="text" className="form-control" id="taxcode" {...register('taxCode', { required: true, pattern: /^[0-9]{10,13}$/ })} disabled />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-key" viewBox="0 0 16 16">
                      <path d="M0 8a4 4 0 0 1 7.465-2H14a.5.5 0 0 1 .354.146l1.5 1.5a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0L13 9.207l-.646.647a.5.5 0 0 1-.708 0L11 9.207l-.646.647a.5.5 0 0 1-.708 0L9 9.207l-.646.647A.5.5 0 0 1 8 10h-.535A4 4 0 0 1 0 8zm4-3a3 3 0 1 0 2.712 4.285A.5.5 0 0 1 7.163 9h.63l.853-.854a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.793-.793-1-1h-6.63a.5.5 0 0 1-.451-.285A3 3 0 0 0 4 5z" />
                      <path d="M4 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0z" />
                    </svg>
                  </span>
                  {errors.taxCode && errors.taxCode?.type === 'required' && <span className="message-errors">Mời nhập mã số thuế</span>}
                  {errors.taxCode && errors.taxCode?.type === 'pattern' && <span className="message-errors">Mã số thuế không hợp lệ</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="found_date" className="mb-0">
                    Ngày thành lập
                  </label>
                  <input type="text"
                    // className={`form-control datepicker-date ${errors.foundDate ? 'is-invalid' : ''} `} 
                    className="form-control datepicker-date"
                    id="found_date" autoComplete="disable" {...register('foundDate', { required: true, validate: isInValidFounded })} />
                  <span className="input_icon">
                    <IconCalendar2 />
                  </span>
                  {errors.foundDate && errors.foundDate?.type === 'required' && <span className="message-errors">Mời nhập ngày thành lập</span>}
                  {errors.foundDate && errors.foundDate?.type === 'validate' && <span className="message-errors">Ngày thành lập phải lớn hơn ngày sinh</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="addressCompany" className="mb-0">
                    Địa chỉ
                  </label>
                  <input type="text"
                    className="form-control"
                    id="addressCompany" autoComplete="disable" {...register('address')} disabled />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-geo" viewBox="0 0 16 16">
                      <path
                        fillRule="evenodd"
                        d="M8 1a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM4 4a4 4 0 1 1 4.5 3.969V13.5a.5.5 0 0 1-1 0V7.97A4 4 0 0 1 4 3.999zm2.493 8.574a.5.5 0 0 1-.411.575c-.712.118-1.28.295-1.655.493a1.319 1.319 0 0 0-.37.265.301.301 0 0 0-.057.09V14l.002.008a.147.147 0 0 0 .016.033.617.617 0 0 0 .145.15c.165.13.435.27.813.395.751.25 1.82.414 3.024.414s2.273-.163 3.024-.414c.378-.126.648-.265.813-.395a.619.619 0 0 0 .146-.15.148.148 0 0 0 .015-.033L12 14v-.004a.301.301 0 0 0-.057-.09 1.318 1.318 0 0 0-.37-.264c-.376-.198-.943-.375-1.655-.493a.5.5 0 1 1 .164-.986c.77.127 1.452.328 1.957.594C12.5 13 13 13.4 13 14c0 .426-.26.752-.544.977-.29.228-.68.413-1.116.558-.878.293-2.059.465-3.34.465-1.281 0-2.462-.172-3.34-.465-.436-.145-.826-.33-1.116-.558C3.26 14.752 3 14.426 3 14c0-.599.5-1 .961-1.243.505-.266 1.187-.467 1.957-.594a.5.5 0 0 1 .575.411z"
                      />
                    </svg>
                  </span>
                  {/* {errors.address && errors.address?.type === 'required' && <span className="message-errors">Mời nhập địa chỉ</span>} */}
                </div>
              </div>
            </div>
            <div className="text-center">
              <button className="btn btn_new mb-4" disabled={isSubmitting || isUploading}>
                {isSubmitting ? (
                  <>
                    <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                  </>
                ) : (
                  <>Cập nhật thông tin</>
                )}
              </button>
            </div>
          </form>
        </div>
      )}
      <div className="account_section">
        <div className="as_header">
          <h4>Cài đặt thông báo</h4>
        </div>
        <div className="row as_noti mb-3">
          <div className="col">
            <h5>Nhận thông báo qua email</h5>
            <p>MContract sẽ gửi thông báo hợp đồng mới, phản hồi mới, hợp đồng sắp hết hạn,... qua email service@mcontract.vn</p>
          </div>
          <div className="col-auto">
            <div className="custom-control custom-switch custom-switch-md">
              <input type="checkbox" checked={acceptEmail ? true : user.acceptEmailNotification ? true : false} className="custom-control-input" id="customSwitch1" onChange={e => AcceptEmailNoti(e)} />
              <label className="custom-control-label" htmlFor="customSwitch1" style={{ cursor: 'pointer' }}></label>
            </div>
          </div>
        </div>
      </div>
      <UploadLogoModal show={showUploadModal} isLogo={true} image={image} onHide={handleClose} onSubmit={onSubmitLogo} uploading={isUploading} />
      {/* <UploadAvatarModal show={showUploadModal} image={image} onHide={handleClose} onSubmit={onSubmitLogo} /> */}
    </>
  );
}

export default FormCompany;
