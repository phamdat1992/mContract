import logo_v from "@/assets/images/logo_v.webp";
import signup from "@/assets/images/signup.svg";
import AuthService from "@Services/auth";
import { useState, useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { Link, useHistory } from "react-router-dom";
import { CLIENT_DOMAIN } from "@Consts/domain";
import { IconEnvelop } from "@Components/Icon";
import { setModal } from '@Redux/Actions/AlertModal';
import { addToast } from '@Redux/Actions/AlertToast';
import { useDispatch, useSelector } from 'react-redux';

function PageSignup() {
   const dispatch = useDispatch();
   const [isRequesting, setIsRequesting] = useState(false);
   const {
      register,
      handleSubmit,
      formState: { errors },
   } = useForm();
   const history = useHistory();

   function onSubmit(data) {
      setIsRequesting(true);
      data.email = data.email.toLowerCase().trim();
      AuthService.signup(data)
         .then((res) => {
            history.push("/dang-ki/ma-xac-thuc", data);
            setIsRequesting(false);
         })
         .catch((err) => {
            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }))
            setIsRequesting(false);
         });
   }

   function goHome() {
      window.location.href = CLIENT_DOMAIN;
   }
   useEffect(() => {
      document.title = "MContract"
   }, []);
   return (
      <div className="container-fluid main_wrapper" style={{ background: "#FFF", minHeight: "100vh" }}>
         <div className="row">
            <div className="col-12 col-lg pl-0 d-none d-lg-block">
               <div className="site_left">
                  <div className="site_left_wrapper">
                     <div className="site_left_content">
                        <div className="slbtn_area">
                           <h4>Bạn đã có tài khoản?</h4>
                           <Link to="/dang-nhap" className="btn btn_sec2">
                              Đăng nhập
                           </Link>
                        </div>
                        <img className="img-fluid" src={signup} alt="" loading="lazy" />
                     </div>
                  </div>
               </div>
            </div>
            <div className="col-12 col-lg site_right">
               <div className="site_form">
                  <div className="text-center">
                     <a href="javascript:void(0)" title="MContract ">
                        <img className="site_logo img-fluid" alt="" src={logo_v} loading="lazy" />
                     </a>
                  </div>
                  <h1 className="site_title">Đăng ký tài khoản</h1>
                  <form onSubmit={handleSubmit(onSubmit)}>
                     <div className="form-group">
                        <label htmlFor="username" className="mb-0">
                           Địa chỉ Email
                        </label>
                        {/* /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i */}
                        <input type="text" className="form-control" id="username" autoComplete="off" {...register("email", { required: true, pattern: /^([\s]{0,})[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}([\s]{0,})$/g })} />
                        <span className="input_icon">
                           <IconEnvelop />
                        </span>
                        {errors.email && errors.email?.type === "required" && <span className="message-errors">Mời nhập email</span>}
                        {errors.email && errors.email?.type === "pattern" && <span className="message-errors">Email không hợp lệ</span>}
                     </div>
                     <div className="form-group mt-2">
                        Bằng việc đăng ký bạn đã đồng ý với{" "}
                        <a href={`${CLIENT_DOMAIN}/dieu-khoan`} target="_blank" rel="noreferrer">
                           điều khoản
                        </a>{" "}
                        và{" "}
                        <a href={`${CLIENT_DOMAIN}/chinh-sach`} rel="noreferrer" target="_blank">
                           chính sách bảo mật
                        </a>{" "}
                        của MContract
                     </div>
                     <div className="form_btn text-center mt-4">
                        <a onClick={goHome} className="btn btn_sec1 back_btn">
                           Quay lại
                        </a>
                        <button disabled={isRequesting} className="btn btn_pri1 submit_btn">
                           {isRequesting ? (
                              <>
                                 <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                              </>
                           ) : (
                              <>Tiếp tục</>
                           )}
                        </button>
                     </div>
                  </form>

                  <div className="bl_area text-center d-block d-lg-none">
                     <p className="mb-0">Bạn đã có tài khoản?</p>
                     <Link to="/dang-nhap" className="link_site">
                        Đăng nhập
                     </Link>
                  </div>
               </div>
            </div>
         </div>
      </div>
   );
}

export { PageSignup };
