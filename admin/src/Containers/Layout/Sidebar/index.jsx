import { IconCancelDocument, IconCheckDocument, IconClock, IconDocuments, IconFailDocument, IconFailShield, IconHourclass, IconPlus, IconTag } from "@Components/Icon";
import { ContractCreateButton } from "@Containers/Contract";
import { TagModal } from "@Containers/Tag";
import { useEffect, useState } from "react";
import PerfectScrollbar from "react-perfect-scrollbar";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useHistory, useLocation } from "react-router-dom";

function SidebarIcon({ iconName }) {
   switch (iconName) {
      case "DOCUMENTS":
         return <IconDocuments />;
      case "CLOCK":
         return <IconClock />;
      case "HOURCLASS":
         return <IconHourclass />;
      case "FAIL_SHIELD":
         return <IconFailShield />;
      case "CHECK_DOCUMENT":
         return <IconCheckDocument />;
      case "FAIL_DOCUMENT":
         return <IconFailDocument />;
      case "CANCEL_DOCUMENT":
         return <IconCancelDocument />;
      default:
         return <></>;
   }
}

function SideBar(props) {
   const statistics = useSelector((state) => state.data.statistics);
   const menus = useSelector((state) => state.menu.menu);
   const tags = useSelector((state) => state.data.tags);
   const { pathname, search } = useLocation();

   const pathActive = ["/", "/trang-chu"];
   const history = useHistory();

   // [ "/dashboard/policy", "/dashboard/policy-detail", "/dashboard/security", "/account-info"];
   // const pathActive = [];

   const [show, setShow] = useState(false);

   const handleClose = () => setShow(false);

   const handleShow = () => setShow(true);
   const statisStatus = (value) => {
      // if (value == "ALL") {
      //    let sum = 0;
      //    for (let i = 0; i < statistics.length; i++) {
      //       sum = sum + statistics[i].count;
      //    }
      //    return sum;
      // } else {
      for (let i = 0; i < statistics.length; i++) {
         if (statistics[i].status === value) {
            return statistics[i].count;
         }
         // }
      }
      return 0;
   };

   function listContractTag(tag) {
      const params = {
         tagId: tag.id
      };
      Object.keys(params).forEach(key => {
         if (!params[key]) {
            delete params[key];
         }
      });
      history.push('/hop-dong-theo-the?' + new URLSearchParams(params).toString());
   }

   function isActiveTag(tag) {
      const params = new URLSearchParams(search);
      return params.get('tagId') == tag.id
   }

   return (
      <>
         <aside id="left_sidebar">
            <div className="text-center">
               <ContractCreateButton />
            </div>
            <PerfectScrollbar>
               <ul className="list-unstyled">
                  <li className="sidebar_item">
                     <NavLink exact to="/trang-chu" isActive={() => pathActive.includes(pathname)}>
                        <span className="si_icon">
                           <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-house-door" viewBox="0 0 16 16">
                              <path d="M8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4.5a.5.5 0 0 0 .5-.5v-4h2v4a.5.5 0 0 0 .5.5H14a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146zM2.5 14V7.707l5.5-5.5 5.5 5.5V14H10v-4a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5v4H2.5z" />
                           </svg>
                        </span>
                        <span className="si_name">Trang chủ</span>
                     </NavLink>
                  </li>
                  {menus.map((menu, index) => (
                     <li className="sidebar_item" key={index}>
                        <NavLink to={menu.path}>
                           <span className="si_icon">
                              <SidebarIcon iconName={menu.icon} />
                           </span>
                           <span className="si_name">{menu.title}</span>
                           {statistics && <span className="si_count">{statisStatus(menu.status)}</span>}
                        </NavLink>
                     </li>
                  ))}
               </ul>
               <hr />
               <h4>DANH SÁCH THẺ</h4>
               <ul className="list-unstyled tags_list">
                  {tags &&
                     tags.map((tag, index) => (
                        <li onClick={() => listContractTag(tag)} className="sidebar_item" key={index}>
                           <NavLink exact to={`/hop-dong-theo-the?tagId=${tag.id}`} isActive={() => isActiveTag(tag)} >
                              <span className="si_icon">
                                 <IconTag fill={tag.colorCode} width="20px" height="20px" />
                              </span>
                              <span className="si_name">{tag.name}</span>
                              <span className="si_count">{tag.countContract || 0}</span>
                           </NavLink>
                        </li>
                     ))}
                  <li className="sidebar_item tag_add">
                     <a href="javascript:void(0);" onClick={handleShow}>
                        <span className="si_icon">
                           <IconPlus width="24" height="24" />
                        </span>
                        <span className="si_name">Quản lý thẻ</span>
                     </a>
                     <TagModal show={show} onHide={handleClose}></TagModal>
                  </li>
               </ul>
            </PerfectScrollbar>
         </aside>
      </>
   );
}

export default SideBar;
