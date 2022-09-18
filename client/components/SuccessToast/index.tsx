import React, { useEffect, useState } from 'react';
import styles from "./index.module.scss";

const SuccessToast = () => {
    function fadeOut(elem: any, ms: any) {
        if (!elem)
            return;
        if (ms) {
            var opacity = 1;
            var timer = setInterval(function () {
                opacity -= 50 / ms;
                if (opacity <= 0) {
                    clearInterval(timer);
                    opacity = 0;
                    elem.style.display = "none";
                    elem.style.visibility = "hidden";
                }
                elem.style.opacity = opacity;
                elem.style.filter = "alpha(opacity=" + opacity * 100 + ")";
            }, 50);
        }
        else {
            elem.style.opacity = 0;
            elem.style.filter = "alpha(opacity=0)";
            elem.style.display = "none";
            elem.style.visibility = "hidden";
        }
    }
    useEffect(() => {
        setTimeout(() => {
            const el = document.querySelector('.toast-contact');
            fadeOut(el, 350);
        }, 3000);
    }, [])
    return (
        <div className={`${styles.toast_site} toast-contact shadow`}>
            <div className={`${styles.toast_icon} ${styles.bg_site}`}>
                <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="bi bi-check-lg" viewBox="0 0 16 16">
                    <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.065.02L3.217 8.384a.757.757 0 0 1 0-1.06.733.733 0 0 1 1.047 0l3.052 3.093 5.4-6.425a.247.247 0 0 1 .02-.022Z" fill="#fff" />
                </svg>
            </div>
            <div className="p-3">
                <p className="text-center">Gửi liên hệ thành công!</p>
            </div>
            <div className={`${styles.toast_footer} ${styles.bg_site}`}>
                <div className={`${styles.footer_arrow}`}></div>
            </div>
        </div>
    )
}
export default SuccessToast;