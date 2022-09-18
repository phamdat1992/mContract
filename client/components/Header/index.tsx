import Link from "next/link";
import { useEffect, useState } from 'react';
import { Collapse } from "react-bootstrap";
import { useRouter } from 'next/router';

const Header = () => {
   const [open, setOpen] = useState(false);
   const router = useRouter();
   useEffect(() => {
   }, [router])
   return (
      <header className="header-area" id="home">
         <div className="container">
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
               <Link href="/">
                  <a className="navbar-brand">
                     <img src="images/logo.svg" alt="Logo" loading="lazy" />
                  </a>
               </Link>
               <button
                  onClick={() => setOpen(!open)}
                  aria-controls="example-collapse-text"
                  aria-expanded={open}
                  className="navbar-toggler btn-custom" type="button"
               >
                  <span className="navbar-toggler-icon"></span>
               </button>
               <Collapse in={open}>
                  <div className="navbar-collapse">
                     <ul className="navbar-nav ml-auto">
                        <li className={`nav-item ${router.pathname == '/' ? 'active' : ''}`}>
                           <Link href="/">
                              <a className="nav-link">Giới thiệu <span className="sr-only">(current)</span></a>
                           </Link>
                        </li>
                        <li className="nav-item">
                           <Link href="/#signature-process">
                              <a className="nav-link">Quy trình ký số</a>
                           </Link>
                        </li>
                        <li className="nav-item">
                           <Link href="/#tool">
                              <a className="nav-link">Công cụ</a>
                           </Link>
                        </li>
                        <li className="nav-item">
                           <Link href="/#contact">
                              <a className="nav-link">Liên hệ</a></Link>
                        </li>
                        <li className={`nav-item ${router.pathname == '/hoi-dap' ? 'active' : ''}`}>
                           <Link href="hoi-dap">
                              <a className="nav-link">Hỗ trợ</a>
                           </Link>
                        </li>
                        <li className="nav-item">
                           <a className="nav-link" href="https://mcontract.vn/dang-ki">Đăng ký</a>
                        </li>
                        <li>
                           <a className="btn btn-primary btn_new ml-2" href="https://mcontract.vn/dang-nhap">Đăng nhập</a>
                        </li>
                     </ul>
                  </div>
               </Collapse>
            </nav>
         </div>
      </header>
   )
}
export default Header;