import logo from "@/assets/images/logo_w.webp";
import { IconLogout, IconSetting, IconSupport } from "@Components/Icon";
import ContractFilter from "@Containers/Contract/ContractFilter";
import NotificationDropdown from "@Containers/Layout/NotificationDropdown";
import AuthService from "@Services/auth";
import { remove } from "@Utils/cookie";
import React, { useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { Link, NavLink } from "react-router-dom";
import { letterNameChar } from '@Utils/helpers';
import { addToast } from "@Redux/Actions/AlertToast";

function DefaultHeader() {
   const information = useSelector((state) => state.auth.user);
   const dispatch = useDispatch();
   function handleLogout() {
      AuthService.logout()
         .then((res) => {
            remove('digital_signature_token');
            remove('digital_signature_refresh_token');
            remove('digital_signature_token_type');
            window.location.href = '/dang-nhap';
         })
         .catch((err) => {
            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
         });
   }

   useEffect(() => {
   }, []);

   return (
      <>
         <header id="page_header" className="position-relative">
            <div className="container-fluid">
               <div className="row">
                  <div className="col-auto order-0 pr-0 pr-md-3">
                     <NavLink to="/trang-chu" className="logo_wrap d-flex align-items-center h-100">
                        <img className="header_logo img-fluid" src={logo} alt="" loading="lazy" />
                     </NavLink>
                  </div>
                  <div className="col-12 col-md order-2 order-md-1 search_wrapper">
                     <ContractFilter />
                  </div>
                  <div className="col-auto order-1 order-md-2 ml-auto d-flex align-items-center">
                     <NotificationDropdown />
                     <div className="account_area ml-2">
                        <Dropdown className="dropdow-setting-custom">
                           <Dropdown.Toggle as={CustomToggle}>
                              <span className="mr-1 d-none d-lg-inline">{information.fullName}</span>
                              {information.avatarPath ?
                                 <img src={information.avatarPath ? information.avatarPath : ""} alt="" loading="lazy" onError={(e) => { e.target.onerror = null; e.target.remove() }} className="rounded-circle account_img" />
                                 : <div className="avatar-alt">{letterNameChar(information.fullName)}</div>
                              }
                           </Dropdown.Toggle>
                           <Dropdown.Menu className="dropdown-menu dropdown-menu-right shadow">
                              <Dropdown.Item as={Link} to="/tra-cuu">
                                 <IconSupport />
                                 <span>Hỗ trợ</span>
                              </Dropdown.Item>
                              <Dropdown.Item as={Link} to="/thong-tin-ca-nhan">
                                 <IconSetting />
                                 <span>Cài đặt</span>
                              </Dropdown.Item>
                              <Dropdown.Item onClick={handleLogout}>
                                 <IconLogout />
                                 <span>Đăng xuất</span>
                              </Dropdown.Item>
                           </Dropdown.Menu>
                        </Dropdown>
                     </div>
                  </div>
               </div>
            </div>
         </header>
      </>
   );
}
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
   <a
      className="account_menu"
      href=""
      ref={ref}
      onClick={(e) => {
         e.preventDefault();
         onClick(e);
      }}
   >
      {children}
   </a>
));
export default DefaultHeader;
