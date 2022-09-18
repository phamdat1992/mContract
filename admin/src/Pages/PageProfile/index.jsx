import DefaultFooter from '@Containers/Layout/Footer';
import NavFilter from '@Containers/Layout/NavFilter';
import FormCompany from '@Containers/Profile/ProfileFormCompany';
import FormUser from '@Containers/Profile/ProfileFormUser';
import $ from 'jquery';
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import SimpleBar from "simplebar-react";
import ProfileService from '@Services/profile';
import ContractCreateButtonMobile from '@Containers/Contract/ContractCreateButtonMobile';
import { addToast } from '@Redux/Actions/AlertToast';

function PageProfile(props) {
    const [information, setInformation] = useState({});
    const dispatch = useDispatch();
    useEffect(() => {
        ProfileService.getUser().then(res => {
            setInformation(res.data);
        }).catch(err => {
            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
        })
    }, []);

    useEffect(() => {
        document.title = "Thay đổi thông tin tài khoản";
        $('.datepicker-date').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true,
            weekStart: 1,
            language: "vi",
            todayHighlight: true,
            startDate: "01/01/1900",
        }).on('hide', function () {
            if (!this.firstHide) {
                if (!$(this).is(":focus")) {
                    this.firstHide = true;
                    this.focus();
                }
            } else {
                this.firstHide = false;
            }
        }).on('show', function () {
            if (this.firstHide) {
                $(this).datepicker('hide');
            }
        });
    }, []);
    useEffect(() => {
        document.title = "MContract"
    }, []);
    return (
        <div className="account">
            <SimpleBar id="page_content" className="container-fluid px-24px">
                <div className="account_info_area py-4">
                    <FormUser information={information} />
                    <FormCompany information={information} />
                </div>
                <DefaultFooter />
            </SimpleBar>
            {/* <ChangePassword /> */}
            <NavFilter />
            <ContractCreateButtonMobile />
        </div>

    );
}

export { PageProfile };