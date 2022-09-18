import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { IconChatAdd, IconBoldTag } from '@Components/Icon';
import { useDispatch, useSelector } from 'react-redux';
import ContractService from '@Services/contract';
import React, { useRef, useState, useEffect } from 'react';
import * as Axios from 'axios';
import { setCanceling, setStartComment, updateContract } from '@Redux/Actions/DetailContract';
import ContractStatus from '@Enums/contract-status';
import { setReadyForReloadStatistics } from '@Redux/Actions/Data';
import ConfirmModal from '@Components/ConfirmModal';
import printJS from 'print-js';
import { addToast } from '@Redux/Actions/AlertToast';
const ToolBar = ({ onCommentChanged, onAttachTagSelected }) => {
  const { contract, currentSignerToken, currentSigner, isCanceling } = useSelector(state => state.detailContract);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [isPermitCancel, setIsPermitCancel] = useState(false);
  const dispatch = useDispatch();
  const user = useSelector(state => state.auth.user);

  const printContract = () => {
    printJS({
      printable: contract.fileUrl,
      type: 'pdf',
      showModal: true,
    });
  };
  const onCancelContract = async () => {
    setShowConfirmModal(false);
    try {
      if (user) {
        dispatch(setCanceling(true));
        await ContractService.cancelContract(contract.id, { contractId: parseInt(contract.id) });
        dispatch(updateContract({
          ...contract,
          status: ContractStatus.CANCEL
        }));
        dispatch(setReadyForReloadStatistics(true));
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'SUCCESS_CANCEL_CONTRACT' }));
      } else {
        dispatch(setCanceling(true));
        await ContractService.cancelContractSigner(currentSignerToken);
        dispatch(updateContract({
          ...contract,
          status: ContractStatus.CANCEL
        }));
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'SUCCESS_CANCEL_CONTRACT' }));
      }
      dispatch(setCanceling(false));
    } catch (err) {
      dispatch(setCanceling(false));
    }
  };

  const onStartComment = () => {
    dispatch(setStartComment(true));
  };

  function getFile(fileUrl) {
    return new Promise((resolve, reject) => {
      Axios.get(fileUrl, { responseType: 'blob' })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          console.error(err);
        });
    });
  }

  async function dowloadContract() {
    const blob = await getFile(contract.signFileUrl ? contract.signFileUrl : contract.fileUrl);
    const objectUrl = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = objectUrl;
    link.download = contract.fileName;
    link.click();
  }

  // const isHasSigned = () => {
  //   return (currentSigner && (contract.signerDtos.filter(item => item.email == currentSigner.email))[0].isSigned) || contract.status == ContractStatus.CANCEL || contract.status == ContractStatus.EXPIRED || contract.status == ContractStatus.COMPLETE || contract.status == ContractStatus.AUTHENTICATIONFAIL;
  // }

  const handleClose = () => {
    dispatch(setCanceling(false));
    setShowConfirmModal(false);
  };

  useEffect(() => {
    if (contract) {
      return setIsPermitCancel((currentSigner && (contract.signerDtos.filter(item => item.email == currentSigner.email))[0].isSigned) || contract.status == ContractStatus.CANCEL || contract.status == ContractStatus.EXPIRED || contract.status == ContractStatus.COMPLETE || contract.status == ContractStatus.AUTHENTICATIONFAIL);
    }
  }, [contract]);

  return (
    <>
      <OverlayTrigger overlay={<Tooltip>Thêm nhận xét</Tooltip>} placement="top">
        {({ ref, ...triggerHandler }) => (
          <a style={{ cursor: 'pointer' }} onClick={() => onStartComment(true)} id="add_comment_btn" {...triggerHandler} ref={ref}>
            <IconChatAdd />
          </a>
        )}
      </OverlayTrigger>
      &nbsp;
      <OverlayTrigger overlay={<Tooltip>In hợp đồng</Tooltip>} placement="top">
        {({ ref, ...triggerHandler }) => (
          <a style={{ cursor: 'pointer' }} {...triggerHandler} ref={ref} onClick={() => printContract()}>
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#555" width="20px" height="20px">
              <path d="M0 0h24v24H0V0z" fill="none" />
              <path d="M19 8H5c-1.66 0-3 1.34-3 3v4c0 1.1.9 2 2 2h2v2c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2v-2h2c1.1 0 2-.9 2-2v-4c0-1.66-1.34-3-3-3zm-4 11H9c-.55 0-1-.45-1-1v-4h8v4c0 .55-.45 1-1 1zm4-7c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1zm-2-9H7c-.55 0-1 .45-1 1v2c0 .55.45 1 1 1h10c.55 0 1-.45 1-1V4c0-.55-.45-1-1-1z" />
            </svg>
          </a>
        )}
      </OverlayTrigger>
      &nbsp;
      <OverlayTrigger overlay={<Tooltip>Tải hợp đồng</Tooltip>} placement="top">
        {({ ref, ...triggerHandler }) => (
          <a style={{ cursor: 'pointer' }} {...triggerHandler} ref={ref} onClick={() => dowloadContract()}>
            {/* href="" download */}
            <svg viewBox="0 0 24 24" fill="#555" width="20px" height="20px">
              <path d="M0 0h24v24H0V0z" fill="none" />
              <path d="M16.59 9H15V4c0-.55-.45-1-1-1h-4c-.55 0-1 .45-1 1v5H7.41c-.89 0-1.34 1.08-.71 1.71l4.59 4.59c.39.39 1.02.39 1.41 0l4.59-4.59c.63-.63.19-1.71-.7-1.71zM5 19c0 .55.45 1 1 1h12c.55 0 1-.45 1-1s-.45-1-1-1H6c-.55 0-1 .45-1 1z" />
            </svg>
          </a>
        )}
      </OverlayTrigger>
      &nbsp;
      {user && (
        <OverlayTrigger overlay={<Tooltip>Danh sách thẻ</Tooltip>} placement="top">
          {({ ref, ...triggerHandler }) => (
            <span data-toggle="modal" data-target="#tagsModal">
              <a style={{ cursor: 'pointer' }} {...triggerHandler} ref={ref} onClick={onAttachTagSelected}>
                <IconBoldTag width="20px" height="20px" />
              </a>
            </span>
          )}
        </OverlayTrigger>
      )}
      &nbsp;
      {
        !isPermitCancel &&
        <OverlayTrigger overlay={<Tooltip>Hủy hợp đồng</Tooltip>} placement="top">
          {({ ref, ...triggerHandler }) => (
            <a style={{ cursor: isCanceling ? 'progress' : 'pointer' }} {...triggerHandler} ref={ref} onClick={() => setShowConfirmModal(true)} >
              <svg viewBox="0 0 24 24" fill="#555" width="20px" height="20px">
                <path d="M0 0h24v24H0V0z" fill="none" />
                <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V9c0-1.1-.9-2-2-2H8c-1.1 0-2 .9-2 2v10zM18 4h-2.5l-.71-.71c-.18-.18-.44-.29-.7-.29H9.91c-.26 0-.52.11-.7.29L8.5 4H6c-.55 0-1 .45-1 1s.45 1 1 1h12c.55 0 1-.45 1-1s-.45-1-1-1z" />
              </svg>
            </a>
          )}
        </OverlayTrigger>
      }
      <ConfirmModal show={showConfirmModal} contractsName={contract.title} userName={currentSigner.fullName} onHide={handleClose} onSubmit={onCancelContract} />
    </>
  );
};
export default ToolBar;
