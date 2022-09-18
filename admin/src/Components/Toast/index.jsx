import React, { useEffect, useRef, useState } from 'react';
import $ from 'jquery';
import { IconToastBug, IconToastBugSystem, IconToastCheckUser, IconToastCode, IconToastExpired, IconToastNoResources, IconToastNotFile, IconToastSuccess, IconToastSuccessContract, IconToastNotReadFile, IconToastSuccessInfor, IconToastUSBToken, IconToastUSBTokenLogin, IconToastSaveFile } from '@Components/Icon';
const ToastIcon = ({ type }) => {
    switch (type) {
        case 'ERROR':
            return <IconToastBug />;
        case 'ERROR_SIGN':
            return <IconToastBug />;
        case 'CODE_EXPIRED':
            return <IconToastExpired />;
        case 'OTP_FAILED':
            return <IconToastExpired />;
        case 'ACCOUNT_NOTFOUND':
            return <IconToastNoResources />;
        case 'NO_RESOURCE':
            return <IconToastNoResources />;
        case 'NOT_FILE':
            return <IconToastNotFile />;
        case 'NOT_READ_FILE':
            return <IconToastNotReadFile />;
        case 'BUG_SAVE_FILE':
            return <IconToastSaveFile />;
        case 'ERROR_SYSTEM':
            return <IconToastBugSystem />;
        case 'USER_EXIST':
            return <IconToastCheckUser />;
        case 'ERROR_AUTHOR':
            return <IconToastCode />;
        case 'SUCCESS_CONTACT':
            return <IconToastSuccessInfor />;
        case 'SUCCESS_INFOR':
            return <IconToastSuccessInfor />;
        case 'SUCCESS_SIGN_CONTRACT':
            return <IconToastSuccessContract />;
        case 'ATTACH_USBTOKEN_SIGN':
            return <IconToastUSBToken />;
        case 'ATTACH_USBTOKEN_SIGNUP':
            return <IconToastUSBTokenLogin />;
        case 'SUCCESS_CANCEL_CONTRACT':
            return <IconToastSuccess />;
        default:
            return <IconToastSuccess />;
    }
};
const ToastContent = ({ type, message = 'Lỗi hệ thống', contractName = '', isCreateContract }) => {
    switch (type) {
        case 'ERROR':
            return <p class="text-center">{message}</p>;
        case 'ERROR_SIGN':
            return <><p>Trong quá trình ký kết <b>“{contractName}”</b> đã phát sinh lỗi.</p>
                <p>Quý khách vui lòng thử lại!</p></>
        case 'CODE_EXPIRED':
            return <p class="text-center">Mã xác thực không hợp lệ hoặc đã hết hạn.</p>;
        case 'OTP_FAILED':
            return <p class="text-center">Mã xác thực không hợp lệ hoặc đã hết hạn.</p>;
        case 'ACCOUNT_NOTFOUND':
            return <p class="text-center">Tài khoản chưa đăng kí.</p>;
        case 'NO_RESOURCE':
            return <p class="text-center">Tài nguyên không tồn tại.</p>;
        case 'NOT_FILE':
            return <p class="text-center">Không tìm thấy Hợp đồng.</p>;
        case 'NOT_READ_FILE':
            return <p class="text-center">Hệ thống không đọc được tệp tin Hợp đồng.</p>;
        case 'BUG_SAVE_FILE':
            return <p class="text-center">Lỗi trong quá trình lưu Hợp đồng.</p>;
        case 'ERROR_SYSTEM':
            return <p class="text-center">Hệ thống phát sinh lỗi.</p>;
        case 'USER_EXIST':
            return <p class="text-center">Tài khoản đã tồn tại!</p>;
        case 'ERROR_AUTHOR':
            return <>
                <p>Quá trình {isCreateContract ? 'tạo' : 'ký'} <b>“{contractName}”</b> bị sai xác thực.
                </p>
                <p>Lỗi {message}</p>
                <p>Vui lòng kiểm tra thông tin USB Token.</p>
            </>
        case 'SUCCESS_CONTACT':
            return <p class="text-center">Gửi liên hệ thành công!</p>;
        case 'SUCCESS_INFOR':
            return <p class="text-center">Cập nhật thông tin thành công.</p>;
        case 'SUCCESS_SIGN_CONTRACT':
            return <p class="text-center" >Ký kết thành công <b>“{contractName}”</b>.</p>;
        case 'ATTACH_USBTOKEN_SIGN':
            return <p class="text-center">Vui lòng <b>cắm USB Token</b> để tiếp tục ký số!</p>;
        case 'ATTACH_USBTOKEN_SIGNUP':
            return <p class="text-center">Vui lòng <b>cắm USB Token</b> để hoàn tất đăng ký tài khoản <b><a href="https://mcontract.vn/" target="_blank">MContract</a></b>!</p>;
        case 'SUCCESS_CANCEL_CONTRACT':
            return <p class="text-center">Hủy Hợp đồng thành công!</p>;
        default:
            return <p class="text-center">{message}</p>;
    }
}


const Toast = ({ id, contractName, type = 'ERROR', message, color, onDestroy, isCreateContract = false }) => {
    const setClassColor = (color) => {
        switch (color) {
            case 'red':
                return 'bg_site_bug';
            case 'green':
                return 'bg_site_contract';
            case 'purple':
                return 'bg_site_code';
            default:
                return 'bg_site';
        }
    }

    useEffect(() => {
        setTimeout(function () {
            $('.toast_site').fadeOut(350);
            setTimeout(() => {
                onDestroy(id);
            }, 350);
        }, 3000);
    }, []);

    return <>
        <div className="contain-toast">
            <div className="toast_site shadow">
                <div className={`toast_icon ${setClassColor(color)}`}>
                    <ToastIcon type={type} />
                </div>
                <div className="p-3">
                    <ToastContent contractName={contractName} type={type} message={message} isCreateContract={isCreateContract} />
                </div>
                <div className={`toast_footer ${setClassColor(color)}`} >
                    <div className="footer_arrow"></div>
                </div>
            </div>
        </div>
    </>
}
export default Toast
