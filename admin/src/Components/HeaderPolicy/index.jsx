import logo_en from '@/assets/images/english.webp';
import logo_vi from '@/assets/images/vietnam.webp';
import propsType from 'prop-types';
import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

HeaderPolicy.propsType = {
    title: propsType.string,
    titleEn: propsType.string
}

function HeaderPolicy(props) {
    function changeEnglish() {
        document.getElementById('en').style.display = 'none';
        document.getElementById('vi').style.display = 'block';
        document.getElementsByClassName('content_vi')[0].style.display = 'none';
        document.getElementsByClassName('content_en')[0].style.display = 'block';
        document.getElementsByClassName('content_vi')[1].style.display = 'none';
        document.getElementsByClassName('content_en')[1].style.display = 'block';
    }
    function changeVietNam() {
        document.getElementById('en').style.display = 'block';
        document.getElementById('vi').style.display = 'none';
        document.getElementsByClassName('content_vi')[0].style.display = 'block'
        document.getElementsByClassName('content_en')[0].style.display = 'none'
        document.getElementsByClassName('content_vi')[1].style.display = 'block'
        document.getElementsByClassName('content_en')[1].style.display = 'none'
    }
    useEffect(() => {
        document.body.style.backgroundColor = "#fff";
        return () => {
            document.body.style.backgroundColor = '#eef5f9';
        };
    }, []);
    return (
        <>
            <div className="language">
                <button type="button" id="vi" className="btn btn-primary" onClick={changeVietNam} >
                    <img src={logo_vi} className="rounded-circle" alt="" loading="lazy" /> <span className="name_language">Tiếng Việt</span>
                </button>
                <button type="button" id="en" className="btn btn-primary" onClick={changeEnglish}>
                    <img src={logo_en} className="rounded-circle" alt="" loading="lazy" /> <span className="name_language">English</span>
                </button>
            </div>
            <nav id="navbar-example2" className="navbar navbar-light bg-light">
                <ul className="nav nav-pills">
                    <li className="nav-item">
                        <Link className="nav-link nav-end" to="/tra-cuu">
                            Chính sách và Hỏi đáp  &gt;</Link>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link na_end nav-end">
                            <span className="content_vi" > &nbsp;{props.title}</span><span className="content_en" > &nbsp;{props.titleEn}</span></a>
                    </li>
                </ul>
            </nav>
        </>
    );
}

export default HeaderPolicy;