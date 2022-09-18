import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import ProfileService from '@Services/profile';
import { IconCalendar2 } from '@Components/Icon';
import GeneralService from '@Services/general';
import AuthService from '@Services/auth';
import { useSelector, useDispatch } from 'react-redux';
import Select from '@Components/Select';
import { Spinner } from 'react-bootstrap';
import { setUser } from '@Redux/Actions/Auth';
import { addToast } from '@Redux/Actions/AlertToast';
import { splitSpace } from '@Utils/helpers';
import UploadLogoModal from '@Components/UploadLogoModal';

function FormUser() {
  const information = useSelector(state => state.auth.user);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [image, setImage] = useState('');
  const dispatch = useDispatch();
  const {
    register,
    handleSubmit,
    setValue,
    getValues,
    formState: { errors },
  } = useForm({
    defaultValues: {
      fullName: information.fullName,
      birthDate: information.birthDate,
      districtCode: information.districtCode,
      address: information.address,
      idNo: information.idNo,
      issuedOn: information.issuedOn,
      phoneNumber: information.phoneNumber,
      proviceCode: information.proviceCode,
      sex: information.sex,
      wardCode: information.wardCode,
      avatarPath: information.avatarPath ? information.avatarPath : '',
    },
  });
  const [provinces, setProvinces] = useState([]);

  const [districts, setDistricts] = useState([]);
  const [isLoadingDistrict, setIsLoadingDistrict] = useState(false);

  const [wards, setWards] = useState([]);
  const [isLoadingWard, setIsLoadingWard] = useState(false);

  const [submitting, setSubmitting] = useState(false);
  const [uploading, setUploading] = useState(false);
  const onSubmit = async data => {
    try {
      setSubmitting(true);
      const newObj = splitSpace(data, ['birthDate', 'proviceCode', 'wardCode', 'districtCode', 'issuedOn']);
      await ProfileService.updateUser(newObj);
      const res = await AuthService.profile();
      dispatch(setUser(res.data));
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'SUCCESS_INFOR' }));
    } catch (e) {
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red" }));
    } finally {
      setSubmitting(false);
    }
  };

  const [gender, setGender] = useState(() => {
    return information.sex;
  });

  const onChangeAvatar = async e => {
    if (e.target.files.length > 0) {
      const file = e.target.files[0];
      setImage(file);
      setShowUploadModal(true);
    }
  };

  const onSubmitAvatar = async file => {
    if (file) {
      try {
        setUploading(true);
        const formData = new FormData();
        formData.append('file', file);
        const res = await GeneralService.uploadFile(formData);
        setValue('avatarPath', res.data.path);
        await ProfileService.updateUser(getValues());
        const resProfile = await AuthService.profile();
        dispatch(setUser(resProfile.data));
      } catch (e) {
        console.error(e);
      } finally {
        setUploading(false);
      }
    }
  };
  function handleCheckGender(e) {
    information.sex = e.target.value;
    setGender(e.target.value);
  }

  function onProvinceChange(provinceName) {
    if (provinceName) {
      const arr = provinces.filter(item => item.name.toUpperCase() == provinceName.toUpperCase());
      setIsLoadingDistrict(true);
      GeneralService.getDistricts(arr[0].id).then(resDistricts => {
        const arrayDistricts = resDistricts.data;
        setDistricts(arrayDistricts);
        setValue('districtCode', ``);
        setValue('wardCode', ``);
        setIsLoadingDistrict(false);
        setIsLoadingWard(true);
      });
    }
  }

  function onDistrictChange(districtName) {
    if (districtName) {
      const arr = districts.filter(item => `${item.prefix} ${item.name}`.toUpperCase() == districtName.toUpperCase());
      GeneralService.getWards(arr[0].id)
        .then(res => {
          setWards(res.data);
          setValue('wardCode', ``);
          setIsLoadingWard(false);
        })
        .catch(err => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
        });
    }
  }
  const handleClose = () => {
    setShowUploadModal(false);
    const inputFile = document.getElementById('change-logo-avatar-setting');
    inputFile.value = '';
  };
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
  useEffect(() => {
    (async () => {
      const provinceRes = await GeneralService.getProvinces();
      const provinceId = provinceRes.data.filter(p => {
        console.log(p.name, information.proviceCode, p.name.toUpperCase().normalize() == information.proviceCode.toUpperCase().normalize() );
        return p.name.toUpperCase().normalize() == information.proviceCode.toUpperCase().normalize();
      })[0].id;
      const districtRes = await GeneralService.getDistricts(provinceId);
      const districtId = districtRes.data.filter(d => `${(d.prefix.toUpperCase() + " " + d.name.toUpperCase()).normalize()}` == information.districtCode.toUpperCase().normalize())[0].id;
      const wardRes = await GeneralService.getWards(districtId);
      setProvinces(provinceRes.data);
      setDistricts(districtRes.data);
      setWards(wardRes.data);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      {Object.keys(information).length > 0 && (
        <div className="account_section">
          <div className="as_header">
            <h4>Thông tin cá nhân</h4>
          </div>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="row">
              <div className="col-12 col-sm-auto d-flex align-items-center">
                <div className="as_img_wrap">
                  <div className="com_img_wrap d-flex justify-content-center align-items-center">
                    {uploading ?
                      <div className="spinner-upload" style={{ position: 'absolute', zIndex: 1, left: 0, top: 0, width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#FFF', borderRadius: '50%' }}>
                        <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" />
                      </div>
                      : <>
                        <img
                          src={getValues('avatarPath') ? getValues('avatarPath') : ''}
                          className="rounded-circle company_logo"
                          onError={e => {
                            e.target.style.display = 'none';
                          }}
                          alt="Avatar"
                          loading="lazy"
                        />
                        <div className="avatar_img_alt">
                          <span>{GeneralService.getLetterName(information.fullName)}</span>
                        </div>
                      </>
                    }
                  </div>
                  <label htmlFor="change-logo-avatar-setting" className="change_icon" title="Thay đổi logo">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-image" viewBox="0 0 16 16">
                      <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"></path>
                      <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z"></path>
                    </svg>
                  </label>
                  <input type="file" title="Thay đổi logo" id="change-logo-avatar-setting" className="change_icon" onChange={e => onChangeAvatar(e)} accept="image/png, image/jpeg, image/jpg" />
                </div>
              </div>
              <div className="col-12 col-sm">
                <div className="form-group">
                  <label htmlFor="fullname" className="mb-0">
                    Họ tên
                  </label>
                  <input
                    type="text"
                    // className={`form-control ${errors.fullName ? 'is-invalid' : ''} `}
                    className="form-control"
                    id="fullname"
                    autoComplete="disable"
                    {...register('fullName', {
                      required: 'Mời nhập họ tên',
                      maxLength: {
                        value: 100,
                        message: 'Họ tên không được dài quá 100 ký tự',
                      },
                      pattern: {
                        value: /^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/,
                        message: 'Họ tên không hợp lệ',
                      },
                    })}
                  />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" height="22" viewBox="0 0 24 24" width="22" fill="currentColor">
                      <path d="M0 0h24v24H0V0z" fill="none" />
                      <path d="M12 5.9c1.16 0 2.1.94 2.1 2.1s-.94 2.1-2.1 2.1S9.9 9.16 9.9 8s.94-2.1 2.1-2.1m0 9c2.97 0 6.1 1.46 6.1 2.1v1.1H5.9V17c0-.64 3.13-2.1 6.1-2.1M12 4C9.79 4 8 5.79 8 8s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 9c-2.67 0-8 1.34-8 4v2c0 .55.45 1 1 1h14c.55 0 1-.45 1-1v-2c0-2.66-5.33-4-8-4z" />
                    </svg>
                  </span>
                  {errors.fullName && <span className="message-errors">{errors.fullName.message}</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="birthday" className="mb-0">
                    Ngày sinh
                  </label>
                  <input
                    type="text"
                    // className={`form-control datepicker-date ${errors.birthDate ? 'is-invalid' : ''} `}
                    className="form-control datepicker-date"
                    min="1900"
                    id="birthday"
                    autoComplete="disable"
                    {...register('birthDate', { required: true, validate: isInvalidBirthday })}
                  />
                  <span className="input_icon">
                    <IconCalendar2 />
                  </span>
                  {errors.birthDate && errors.birthDate?.type === 'required' && <span className="message-errors">Mời nhập ngày sinh</span>}
                  {errors.birthDate && errors.birthDate?.type === 'validate' && <span className="message-errors">Bạn phải đủ 18 tuổi</span>}
                </div>
                <div className="form-group">
                  <label className="mb-0">Giới tính</label>
                  <div className="gender_area">
                    <div className="custom-control custom-radio custom-control-inline mr-4">
                      <input
                        type="radio"
                        id="gender1"
                        name="gender"
                        className="custom-control-input"
                        checked={gender === 'Nam' ? true : information.sex === 'Nam' ? true : false}
                        onClick={e => {
                          handleCheckGender(e);
                        }}
                        value="Nam"
                        {...register('sex', { required: 'Mời chọn giới tính' })}
                      />
                      <label className="custom-control-label" htmlFor="gender1">
                        Nam
                      </label>
                    </div>
                    <div className="custom-control custom-radio custom-control-inline">
                      <input
                        type="radio"
                        id="gender2"
                        name="gender"
                        className="custom-control-input"
                        checked={gender === 'Nữ' ? true : information.sex === 'Nữ' ? true : false}
                        onClick={e => {
                          handleCheckGender(e);
                        }}
                        value="Nữ"
                        {...register('sex', { required: 'Mời chọn giới tính' })}
                      />
                      <label className="custom-control-label" htmlFor="gender2">
                        Nữ
                      </label>
                    </div>
                  </div>
                </div>
                <div className="form-group">
                  <label htmlFor="identity" className="mb-0">
                    Số CMND/CCCD
                  </label>
                  <input
                    type="text"
                    // className={`form-control ${errors.idNo ? 'is-invalid' : ''} `}
                    className="form-control"
                    id="identity"
                    autoComplete="disable"
                    {...register('idNo', { required: 'Mời nhập số CMND/CCCD', pattern: { value: /^([\s]{0,})[0-9]{9,12}([\s]{0,})$/g, message: 'Số CMND/CCCD không hợp lệ' } })}
                  />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-card-heading" viewBox="0 0 16 16">
                      <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h13zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-13z" />
                      <path d="M3 8.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5zm0-5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-1z" />
                    </svg>
                  </span>
                  {errors.idNo && <span className="message-errors">{errors.idNo.message}</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="valid_date" className="mb-0">
                    Ngày cấp
                  </label>
                  <input
                    type="text"
                    // className={`form-control datepicker-date ${errors.issuedOn ? 'is-invalid' : ''} `}
                    className="form-control datepicker-date"
                    id="valid_date"
                    autoComplete="disable"
                    {...register('issuedOn', { required: true, validate: isInValidFounded })}
                  />
                  <span className="input_icon">
                    <IconCalendar2 />
                  </span>
                  {/* {errors.issuedOn && <span className="message-errors">{errors.issuedOn.message}</span>} */}
                  {errors.issuedOn && errors.issuedOn?.type === 'required' && <span className="message-errors">Mời nhập ngày cấp</span>}
                  {errors.issuedOn && errors.issuedOn?.type === 'validate' && <span className="message-errors">Ngày cấp phải lớn hơn ngày sinh</span>}
                </div>
              </div>
              <div className="col-12 col-sm">
                <div className="form-group">
                  <label htmlFor="phone" className="mb-0">
                    Số điện thoại
                  </label>
                  <input
                    type="tel"
                    // className={`form-control ${errors.phoneNumber ? 'is-invalid' : ''} `}
                    className="form-control"
                    id="phone"
                    autoComplete="disable"
                    {...register('phoneNumber', {
                      required: 'Mời nhập số điện thoại',
                      pattern: {
                        value: /^([\s]{0,})[0][1-9]([0-9]{8})([\s]{0,})$/g,
                        message: 'Số điện thoại không hợp lệ',
                      },
                    })}
                  />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-telephone" viewBox="0 0 16 16">
                      <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z" />
                    </svg>
                  </span>
                  {errors.phoneNumber && <span className="message-errors">{errors.phoneNumber.message}</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="city" className="mb-0">
                    Tỉnh/Thành phố
                  </label>
                  <Select
                    // className={`custom-select ${errors.proviceCode ? 'is-invalid' : ''}`}
                    className="custom-select"
                    register={register}
                    fieldName="proviceCode"
                    validators={{ required: true }}
                    optionData={provinces}
                    currentValue={getValues('proviceCode')}
                    optionValue={item => `${item.name}`}
                    optionText={item => `${item.name}`}
                    onChange={onProvinceChange}
                  />
                  {errors.proviceCode && errors.proviceCode?.type === 'required' && <span className="message-errors">Mời chọn Tỉnh/Thành phố</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="district" className="mb-0">
                    Quận/Huyện
                  </label>
                  <Select
                    disabled={isLoadingDistrict}
                    // className={`custom-select ${errors.districtCode ? 'is-invalid' : ''}`}
                    className="custom-select"
                    register={register}
                    fieldName="districtCode"
                    validators={{ required: true }}
                    optionData={districts}
                    currentValue={getValues('districtCode')}
                    optionValue={item => `${item.prefix} ${item.name}`}
                    optionText={item => `${item.prefix} ${item.name}`}
                    onChange={onDistrictChange}
                  />
                  {errors.districtCode && errors.districtCode?.type === 'required' && <span className="message-errors">Mời chọn Quận/Huyện</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="ward" className="mb-0">
                    Phường/Xã
                  </label>
                  <Select
                    disabled={isLoadingWard}
                    // className={`custom-select ${errors.wardCode ? 'is-invalid' : ''}`}
                    className="custom-select"
                    register={register}
                    fieldName="wardCode"
                    validators={{ required: true }}
                    optionData={wards}
                    currentValue={getValues('wardCode')}
                    optionValue={item => `${item.prefix} ${item.name}`}
                    optionText={item => `${item.prefix} ${item.name}`}
                  />
                  {errors.wardCode && errors.wardCode?.type === 'required' && <span className="message-errors">Mời chọn Phường/Xã</span>}
                </div>
                <div className="form-group">
                  <label htmlFor="address" className="mb-0">
                    Địa chỉ
                  </label>
                  <input
                    type="text"
                    // className={`form-control ${errors.address ? 'is-invalid' : ''} `}
                    className="form-control"
                    id="addressUser"
                    autoComplete="off"
                    {...register('address', {
                      required: 'Mời nhập địa chỉ', pattern: {
                        value: /^([\s]{0,})([^\s.]+\s?){1,}([\s]{0,})$/g,
                        message: 'Mời nhập địa chỉ'
                      }
                    })}
                  />
                  <span className="input_icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-geo" viewBox="0 0 16 16">
                      <path
                        fillRule="evenodd"
                        d="M8 1a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM4 4a4 4 0 1 1 4.5 3.969V13.5a.5.5 0 0 1-1 0V7.97A4 4 0 0 1 4 3.999zm2.493 8.574a.5.5 0 0 1-.411.575c-.712.118-1.28.295-1.655.493a1.319 1.319 0 0 0-.37.265.301.301 0 0 0-.057.09V14l.002.008a.147.147 0 0 0 .016.033.617.617 0 0 0 .145.15c.165.13.435.27.813.395.751.25 1.82.414 3.024.414s2.273-.163 3.024-.414c.378-.126.648-.265.813-.395a.619.619 0 0 0 .146-.15.148.148 0 0 0 .015-.033L12 14v-.004a.301.301 0 0 0-.057-.09 1.318 1.318 0 0 0-.37-.264c-.376-.198-.943-.375-1.655-.493a.5.5 0 1 1 .164-.986c.77.127 1.452.328 1.957.594C12.5 13 13 13.4 13 14c0 .426-.26.752-.544.977-.29.228-.68.413-1.116.558-.878.293-2.059.465-3.34.465-1.281 0-2.462-.172-3.34-.465-.436-.145-.826-.33-1.116-.558C3.26 14.752 3 14.426 3 14c0-.599.5-1 .961-1.243.505-.266 1.187-.467 1.957-.594a.5.5 0 0 1 .575.411z"
                      />
                    </svg>
                  </span>
                  {errors.address && <span className="message-errors">{errors.address.message}</span>}
                </div>
              </div>
            </div>
            <div className="text-center">
              <button className="btn btn_new mb-4">
                {submitting ? (
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
      <UploadLogoModal show={showUploadModal} isLogo={false} image={image} onHide={handleClose} onSubmit={onSubmitAvatar} uploading={uploading} />
      {/* <UploadAvatarModal show={showUploadModal} title="Cập nhật ảnh đại diện" image={image} onHide={handleClose} onSubmit={onSubmitAvatar} /> */}
    </>
  );
}

export default FormUser;
