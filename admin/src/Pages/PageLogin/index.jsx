import login from "@/assets/images/login.svg";
import logo_v from "@/assets/images/logo_v.webp";
import AuthService from "@Services/auth";
import React, { useState, useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { Link, useHistory } from "react-router-dom";
import { CLIENT_DOMAIN } from "@Consts/domain";
import { useSelector, useDispatch } from "react-redux";
import { Redirect } from "react-router-dom";
import { addToast } from "@Redux/Actions/AlertToast";

function PageLogin() {
   const [isRequesting, setIsRequesting] = useState(false);
   const user = useSelector((state) => state.auth.user);
   const dispatch = useDispatch();
   const {
      register,
      handleSubmit,
      formState: { errors },
   } = useForm();
   const history = useHistory();

   useEffect(() => {
      document.title = "MContract"
   }, []);

   if (user) {
      return <Redirect to="/trang-chu" />;
   }

   const onSubmit = async (data) => {
      try {
         setIsRequesting(true);
         data.email = data.email.toLowerCase().trim();
         await AuthService.login(data);
         history.push("/dang-nhap/ma-xac-thuc", data);
      } catch (err) {
         dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      } finally {
         setIsRequesting(false);
      }
   }

   return (
      <div className="container-fluid main_wrapper" style={{ background: "#FFF", minHeight: "100vh" }}>
         <div className="row">
            <div className="col-12 col-lg pl-0 d-none d-lg-block">
               <div className="site_left">
                  <div className="site_left_wrapper">
                     <div className="site_left_content">
                        <div className="slbtn_area">
                           <h4>Bạn chưa có tài khoản?</h4>
                           <Link to="/dang-ki" className="btn btn_sec2">
                              Đăng ký ngay
                           </Link>
                        </div>
                        <img className="img-fluid" src={login} alt="" loading="lazy" />
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
                  <h1 className="site_title">Đăng nhập tài khoản</h1>
                  <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
                     {/* <input style={{ display: 'none' }} type="text" name="password" hidden autoComplete="new-password" /> */}
                     <div className="form-group">
                        <label htmlFor="username" className="mb-0">
                           Địa chỉ Email
                        </label>
                        <input type="text" className="form-control" autoComplete="off" id="email" {...register("email", { required: true, pattern: /^([\s]{0,})[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}([\s]{0,})$/g })} />
                        <span className="input_icon">
                           <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-envelope" viewBox="0 0 16 16">
                              <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2zm13 2.383l-4.758 2.855L15 11.114v-5.73zm-.034 6.878L9.271 8.82 8 9.583 6.728 8.82l-5.694 3.44A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.739zM1 11.114l4.758-2.876L1 5.383v5.73z" />
                           </svg>
                        </span>
                        {errors.email && errors.email?.type === "required" && <span className="message-errors d-block">Mời nhập email</span>}
                        {errors.email && errors.email?.type === "pattern" && <span className="message-errors d-block">Email không hợp lệ</span>}
                     </div>
                     <div className="form_btn text-center mt-4">
                        <a href={`${CLIENT_DOMAIN}`} alt="Quay lại" className="btn btn_sec1 back_btn">
                           Quay lại
                        </a>
                        <button disabled={isRequesting} className="btn btn_pri1 submit_btn">
                           {isRequesting ? (
                              <>
                                 <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                              </>
                           ) : (
                              <>Đăng nhập</>
                           )}
                        </button>
                     </div>
                  </form>

                  <div className="bl_area text-center d-block d-lg-none">
                     <p className="mb-0">Bạn chưa có tài khoản?</p>
                     <Link to="/dang-ki" className="link_site">
                        Đăng ký ngay
                     </Link>
                  </div>
               </div>
            </div>
         </div>
      </div>
   );
}
export { PageLogin };
