import { ComponentProps, useEffect, useState } from "react";
import Header from '@/components/Header';
import styles from "./index.module.scss";
const Layout = (props: ComponentProps<any>) => {
   const [isVisible, setIsVisible] = useState(false);
   useEffect(() => {
      document.addEventListener("scroll", function (e) {
         if (window.pageYOffset > 300) {
            setIsVisible(true);
         } else {
            setIsVisible(false);
         }
      });
   })
   function backToTop() {
      window.scrollTo({
         top: 0,
         behavior: "smooth"
      });
   }
   return (
      <div className={styles.Layout}>
         <div id="header_bg"></div>
         <Header />
         {props.children}

         {isVisible ? <>
            < div className={styles.BackToTop}>
               <a href="#" title="Quay lên đầu trang" className={styles.BtnBacck} onClick={() => backToTop()}>
                  <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" className="bi bi-arrow-left-short" viewBox="0 0 16 16">
                     <path fillRule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5z"></path>
                  </svg>
               </a>
            </div>
         </> : <></>}
      </div>

   )
};

export default Layout;
