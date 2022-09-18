import React, { useState } from "react";
import { ContractCreateButton } from "@Containers/Contract";
import { useSelector, useDispatch } from "react-redux";
import ProfileService from '@Services/profile';
import { Spinner } from "react-bootstrap";
import { setUser } from '@Redux/Actions/Auth';
import GeneralService from "@Services/general";
import UploadLogoModal from "@Components/UploadLogoModal";
DbTop.propTypes = {};

function DbTop(props) {
   const [showUploadModal, setShowUploadModal] = useState(false);
   const [image, setImage] = useState("");
   const dispatch = useDispatch();
   const user = useSelector(state => state.auth.user);
   const [uploading, setUploading] = useState(false);

   // const onLogoChange = async (e) => {
   //    if (e.target.files.length > 0) {
   //       try {
   //          setUploading(true);
   //          const f = e.target.files[0];
   //          const formData = new FormData();
   //          formData.append('file', f);
   //          const res = await GeneralService.uploadFile(formData);
   //          const data = {
   //             ...user.company,
   //             ...{ logoPath: res.data.path }
   //          };
   //          const companyRes = await ProfileService.updateCompany(data);
   //          dispatch(setUser({
   //             ...user,
   //             ...{ company: companyRes.data }
   //          }));
   //       } catch (e) {
   //          console.error(e);
   //       } finally {
   //          setUploading(false);
   //       }
   //    }
   // }

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
            setUploading(true);
            const formData = new FormData();
            formData.append('file', file);
            const res = await GeneralService.uploadFile(formData);
            const data = {
               ...user.company,
               ...{ logoPath: res.data.path }
            };
            const companyRes = await ProfileService.updateCompany(data);
            dispatch(setUser({
               ...user,
               ...{ company: companyRes.data }
            }));
         } catch (e) {
            console.error(e);
         } finally {
            setUploading(false);
         }
      }
   }
   const handleClose = () => {
      setShowUploadModal(false);
      const inputFile = document.getElementById('change-logo-company-home');
      inputFile.value = '';
   };
   return (<>
      <div className="db_top shadow-sm">
         <div className="row">
            <div className="col-md-5 d-flex align-items-center">
               <div className="p-3 text-center w-100">
                  <ContractCreateButton />
                  <p className="mb-2">Kết nối không khoảng cách, an toàn theo tiêu chuẩn ký số châu Âu</p>
               </div>
            </div>
            <hr className="d-block d-md-none" />
            <div className="col-md-7">
               <div className="db_tr d-flex align-items-center">
                  <div className="row">
                     <div className="col-auto">
                        <div className="com_img_wrap d-flex justify-content-center align-items-center">
                           {
                              uploading ? <>
                                 <div className="spinner-upload">
                                    <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" />
                                 </div>
                              </> : <>
                                 <img
                                    src={(user.company && user.company.logoPath) ? user.company.logoPath : ""}
                                    className="rounded-circle company_logo"
                                    onError={(e) => {
                                       e.target.onerror = null;
                                       e.target.style.display = 'none';
                                    }}
                                    alt="" loading="lazy"
                                 />
                                 <div className="img_alt">
                                    <span>{GeneralService.getLetterName(user.company.name)}</span>
                                 </div>
                              </>
                           }
                        </div>
                        <a href="javascript:void(0)" className="change_icon" title="Thay đổi logo">
                           <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-image" viewBox="0 0 16 16">
                              <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"></path>
                              <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z"></path>
                           </svg>
                        </a>
                        <label htmlFor="change-logo-company-home" className="change_icon" title="Thay đổi logo" style={{ margin: '0', cursor: 'pointer' }}>
                           <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-image" viewBox="0 0 16 16">
                              <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"></path>
                              <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z"></path>
                           </svg>
                        </label>
                        <input type="file" title="Thay đổi logo" id="change-logo-company-home" className="change_icon" accept="image/png, image/jpeg, image/jpg" style={{ zIndex: '0' }} onChange={onLogoChange} />
                     </div>
                     <div className="col overflow-hidden pl-0">
                        <div className="com_meta com_name text-uppercase">{(user.company && user.company.name) ? user.company.name : ""}</div>
                        <div className="com_meta">
                           <svg xmlns="http://www.w3.org/2000/svg" width="16px" height="16px" fill="currentColor" className="bi bi-geo-alt" viewBox="0 0 16 16">
                              <path d="M12.166 8.94c-.524 1.062-1.234 2.12-1.96 3.07A31.493 31.493 0 0 1 8 14.58a31.481 31.481 0 0 1-2.206-2.57c-.726-.95-1.436-2.008-1.96-3.07C3.304 7.867 3 6.862 3 6a5 5 0 0 1 10 0c0 .862-.305 1.867-.834 2.94zM8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10z" />
                              <path d="M8 8a2 2 0 1 1 0-4 2 2 0 0 1 0 4zm0 1a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
                           </svg>
                           &nbsp;
                           <span title={(user.company && !!user.company.address) ? user.company.address : (user.address ? user.address : "")}>{(user.company && user.company.address) ? user.company.address : (user.address ? user.address : "")}</span>
                        </div>
                        <div className="com_meta">
                           <svg xmlns="http://www.w3.org/2000/svg" width="16px" height="16px" fill="currentColor" className="bi bi-telephone" viewBox="0 0 16 16">
                              <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z" />
                           </svg>
                           &nbsp;
                           <span>{(user.company && !!user.company.phoneNumber) ? user.company.phoneNumber : (user.phoneNumber ? user.phoneNumber : '')}</span>
                        </div>
                        <div className="com_meta">
                           <svg xmlns="http://www.w3.org/2000/svg" width="16px" height="16px" fill="currentColor" className="bi bi-envelope" viewBox="0 0 16 16">
                              <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2zm13 2.383l-4.758 2.855L15 11.114v-5.73zm-.034 6.878L9.271 8.82 8 9.583 6.728 8.82l-5.694 3.44A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.739zM1 11.114l4.758-2.876L1 5.383v5.73z" />
                           </svg>
                           &nbsp;
                           <span title={(user.company && !!user.company.email) ? user.company.email : (user.email ? user.email : '')}>{(user.company && user.company.email) ? user.company.email : (user.email ? user.email : '')}</span>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <UploadLogoModal show={showUploadModal} isLogo={true} image={image} onHide={handleClose} onSubmit={onSubmitLogo} uploading={uploading} />
      {/* <UploadAvatarModal show={showUploadModal} image={image} onHide={handleClose} onSubmit={onSubmitLogo} /> */}
   </>
   );
}

export default DbTop;
