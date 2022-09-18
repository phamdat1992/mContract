import React from 'react';
import PropTypes from 'prop-types';
import { useForm } from 'react-hook-form';

function ChangePassword(props) {
    return (
        <>
           <div className="modal fade modal_site" id="pwModal" tabIndex="-1" aria-labelledby="pwModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                    <div className="modal-content">
                        <div className="modal-header d-block text-center">
                            <h5 className="modal-title" id="pwModalLabel">Thay đổi mật khẩu</h5>
                        </div>
                    <form >
                        <div className="modal-body">
                            <div className="form-group">
                                <label htmlFor="oldpw" className="mb-0">Mật khẩu cũ</label>
                                <input type="password" className="form-control" id="oldpw" />
                                <span className="input_icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22px" height="22px"><g fill="none"><path d="M0 0h24v24H0V0z"></path><path d="M0 0h24v24H0V0z" opacity=".87"></path></g><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm9 14H6V10h12v10zm-6-3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"></path></svg>
                                </span>
                            </div>
                            <div className="form-group">
                                <label htmlFor="newpw" className="mb-0">Mật khẩu mới</label>
                                <input type="password" className="form-control" id="newpw" />
                                <span className="input_icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22px" height="22px"><g fill="none"><path d="M0 0h24v24H0V0z"></path><path d="M0 0h24v24H0V0z" opacity=".87"></path></g><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm9 14H6V10h12v10zm-6-3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"></path></svg>
                                </span>
                            </div>
                            <div className="form-group">
                                <label htmlFor="repw" className="mb-0">Nhập lại mật khẩu mới</label>
                                <input type="password" className="form-control" id="repw" />
                                <span className="input_icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22px" height="22px"><g fill="none"><path d="M0 0h24v24H0V0z"></path><path d="M0 0h24v24H0V0z" opacity=".87"></path></g><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm9 14H6V10h12v10zm-6-3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"></path></svg>
                                </span>
                            </div>
                        </div>
                        <div className="modal-footer new_contract_nav d-block text-center">
                            <button className="btn btn_outline_main mx-2 mx-sm-3" data-dismiss="modal">HỦY BỎ</button>
                            <button className="btn btn_new mx-2 mx-sm-3" data-dismiss="modal">LƯU LẠI</button>
                        </div>
                    </form>
                    </div>
                </div>
            </div> 
        </>
    );
}

export default ChangePassword;