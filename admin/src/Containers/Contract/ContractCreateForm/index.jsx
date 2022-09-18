import { useState } from 'react';
import { ContractCreateFormFile, ContractCreateFormSigner, ContractCreateFormDesign, ContractCreateFormSubject } from '@Containers/Contract';
import logo from '@/assets/images/logo.svg';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setReloadContract } from '@Redux/Actions/Data';
import * as Axios from 'axios';
import { get } from '@Utils/cookie';
import { setStep, setFile } from '@Redux/Actions/CreateContract';

const API_BASE_URL = 'https://mcontract.vn/api';

const ContractCreateFormStep = ({ step, ...props }) => {
  switch (step) {
    case 1:
      return <ContractCreateFormFile {...props} />;
    case 2:
      return <ContractCreateFormSigner {...props} />;
    case 3:
      return <ContractCreateFormDesign {...props} />;
    case 4:
      return <ContractCreateFormSubject {...props} />;
    default:
      return <></>;
  }
};

const ContractCreateForm = ({ onCloseModal }) => {
  const { currentStep, signers, file } = useSelector(state => state.createContract);
  const dispatch = useDispatch();

  const moveToStep = step => {
    dispatch(setStep(step));
  };

  return (
    <>
      <div className="new_contract">
        <div className="row new_contract_header mx-0">
          <div className="close_btn">
            <a onClick={onCloseModal} style={{ cursor: 'pointer' }}>
              &times;
            </a>
          </div>

          <div className="col-auto d-none d-lg-block">
            <img className="img-fluid py-3 py-lg-3 pl-3 pl-lg-3 pr-0" src={logo} alt="" loading="lazy" />
          </div>

          <div className="col">
            <h1 className="m-3 text-uppercase text-center">Tạo hợp đồng</h1>

            <div className="mb-3 large_steps sw sw-theme-arrows sw-justified">
              <ul className="nav">
                <li className="nav-item">
                  <a className={`nav-link inactive ${currentStep == 1 ? 'active' : currentStep > 1 ? 'done' : ''}`} onClick={() => moveToStep(1)}>
                    <b>Bước 1</b>
                    <p className="m-0">Tải lên hợp đồng</p>
                  </a>
                </li>
                <li className="nav-item">
                  <a className={`nav-link inactive ${currentStep == 2 ? 'active' : currentStep > 2 ? 'done' : ''} ${!file.pdf ? 'disable-step' : ''}`} disabled={!file.pdf} onClick={() => (!!file.pdf ? moveToStep(2) : null)}>
                    <b>Bước 2</b>
                    <p className="m-0">Chọn đối tác</p>
                  </a>
                </li>
                <li className="nav-item">
                  <a className={`nav-link inactive ${currentStep == 3 ? 'active' : currentStep > 3 ? 'done' : ''} ${signers.length <= 1 ? 'disable-step' : ''}`} disabled={signers.length <= 1} onClick={() => (signers.length > 1 ? moveToStep(3) : null)}>
                    <b>Bước 3</b>
                    <p className="m-0">Thiết kế hợp đồng</p>
                  </a>
                </li>
                <li className="nav-item">
                  <a className={`nav-link inactive text-center ${currentStep == 4 ? 'active' : currentStep > 4 ? 'done' : ''} ${signers.filter(signer => signer.position.page).length <= 1 ? 'disable-step' : ''}`} disabled={signers.filter(signer => signer.position.page).length <= 1} onClick={() => (signers.filter(signer => signer.position.page).length > 1 ? moveToStep(4) : null)}>
                    <b>Bước 4</b>
                    <p className="m-0">Hoàn tất và gửi</p>
                  </a>
                </li>
              </ul>
            </div>
            <div className="small_steps">
              <div className="progress">
                {currentStep == 1 && <div className="progress-bar w-25" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>}
                {currentStep == 2 && <div className="progress-bar w-50" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>}
                {currentStep == 3 && <div className="progress-bar w-75" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>}
                {currentStep == 4 && <div className="progress-bar w-100" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>}
              </div>
              {currentStep == 1 && <h3>Bước 1: Tải lên hợp đồng</h3>}
              {currentStep == 2 && <h3>Bước 2: Chọn đối tác</h3>}
              {currentStep == 3 && <h3>Bước 3: Thiết kế hợp đồng</h3>}
              {currentStep == 4 && <h3>Bước 4: Hoàn tất và gửi</h3>}
            </div>
          </div>
        </div>
        <ContractCreateFormStep step={currentStep} onCloseModal={onCloseModal} />
        <div className="row new_contract_footer py-2 mx-auto">
          <div className="col-md-auto col-lg text-center text-md-right">HỖ TRỢ KỸ THUẬT</div>
          <div className="col-md col-lg text-center">
            Email: <a href="mailto:support@mcontract.vn">support@mcontract.vn</a>
          </div>
          <div className="col-md-auto col-lg text-center text-md-left">
            Hotline: <a href="tel:0867746979">086 774 6979</a>
          </div>
        </div>
      </div>
    </>
  );
};
export default ContractCreateForm;
