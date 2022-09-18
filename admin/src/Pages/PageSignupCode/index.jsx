import login from "@/assets/images/login.svg";
import logo_v from "@/assets/images/logo_v.webp";
import AuthService from "@Services/auth";
import { useState, useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import { useHistory, useLocation } from "react-router-dom";
import { setUser } from "@Redux/Actions/Auth";
import { addToast } from '@Redux/Actions/AlertToast';
const SECONDS = 60;

function PageSignupCode(props) {
   const {
      register,
      handleSubmit,
      formState: { errors },
   } = useForm();
   const [isRequesting, setIsRequesting] = useState(false);
   const [timeGetCode, setTimeGetCode] = useState(SECONDS);
   const history = useHistory();
   const location = useLocation();
   const dispatch = useDispatch();

   const onSubmit = async (value) => {
      try {
         setIsRequesting(true);
         value.otp = value.otp.trim();
         const data = { ...location.state, ...value };
         const verifyRes = await AuthService.enterCodeRegister(data);
         AuthService.saveToken(verifyRes.data);
         dispatch(setUser(verifyRes.data));
         history.push("/cap-nhat-thong-tin");
      } catch (err) {
         dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      } finally {
         setIsRequesting(false);
      }
   }

   const onGetCode = async () => {
      if (timeGetCode == 60 || timeGetCode == 0) {
         setTimeGetCode(SECONDS);
         try {
            await AuthService.signup(location.state);
         } catch (err) {
            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
         } finally {
         }

      }
   }

   useEffect(() => {
      document.title = "MContract"
   }, []);

   useEffect(() => {
      if (!timeGetCode) return;
      const intervalId = setInterval(() => {
         setTimeGetCode(timeGetCode - 1);
      }, 1000);
      return () => clearInterval(intervalId);
   }, [timeGetCode]);

   return (
      <div className="container-fluid main_wrapper" style={{ background: "#FFF", minHeight: "100vh" }}>
         <div className="row">
            <div className="col-12 col-lg pl-0 d-none d-lg-block">
               <div className="site_left">
                  <div className="site_left_wrapper">
                     <div className="site_left_content">
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
                  <h1 className="site_title" style={{ marginBottom: "15px", textAlign: "center" }}>
                     Nhập mã code đã nhận được từ email
                  </h1>
                  <form onSubmit={handleSubmit(onSubmit)}>
                     <div className="form-group">
                        <input type="text" className="form-control text-center pr-0" placeholder="Nhập mã code" autoComplete="off" {...register("otp", { required: true })} />
                        {errors.otp && errors.otp?.type === "required" && <span className="d-flex message-errors">Mời nhập mã code nhận được</span>}
                        {/* {error && <span className="message-errors">{error}</span>} */}

                     </div>
                     <div className="form_btn text-center mt-4">
                        <a onClick={() => history.goBack()} className="btn btn_sec1 back_btn">
                           Quay lại
                        </a>
                        <button disabled={isRequesting} className="btn btn_pri1 submit_btn">
                           {isRequesting ? (
                              <>
                                 <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                              </>
                           ) : (
                              <>Xác nhận</>
                           )}
                        </button>
                     </div>
                     {
                        timeGetCode != 0 ?
                           <p className="mt-4 a-button text-center">Gửi lại mã code sau: {timeGetCode}s</p>
                           :
                           <p className="mt-4 a-button text-center"> <a type="button" className="again-code" onClick={() => onGetCode()}>Gửi lại mã code</a></p>
                     }
                  </form>
               </div>
            </div>
         </div>
      </div>
   );
}

export { PageSignupCode };
