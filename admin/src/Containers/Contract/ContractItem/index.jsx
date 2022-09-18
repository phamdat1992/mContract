import React, { useEffect, useState } from 'react';
import { IconDelete, IconDot, IconDownload, IconFile, IconPencil, IconRead, IconReceive, IconSend, IconTag, IconUnread } from '@Components/Icon';
import { OverlayTrigger, Tooltip, Dropdown } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { Spinner } from 'react-bootstrap';
import GeneralService from '@Services/general';
import { contractStatusDesc, contractStatusClassName, dateToString, letterNameChar } from '@Utils/helpers';
import ContractStatus from '@Enums/contract-status.js';
import ToolUsb from '@Services/toolUsb';
// import { setContract, updateContract, setCurrentSigner } from '@Redux/Actions/DetailContract';
import { setGlobalSigning } from '@Redux/Actions/ListContract';
import { setInstallToolModal } from '@Redux/Actions/InstallToolModal';
import ContractService from '@Services/contract';
import { addToast } from '@Redux/Actions/AlertToast';
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
    <a
        href="javascript:void(0)"
        className="three_dots"
        id="dropdownMenuButton"
        ref={ref}
        onClick={e => {
            e.stopPropagation();
            onClick(e);
        }}
    >
        {children}
    </a>
));

const ContractItem = ({ contract, index, selectedContracts, checkContracts, onCheck, showAction = true, showAvatar = false, showContractType = false, onUpdateReadStatus, onDeleteContracts, onCancelContracts, onDownloadContracts, onAttachTagContracts, isRequesting }) => {
    const user = useSelector(state => state.auth.user);
    const [isSigning, setIsSigning] = useState(false);
    const { isCanceling, isGlobalSigning } = useSelector(state => state.listContracts);
    const dispatch = useDispatch();

    const history = useHistory();

    const isContractCompleted = contract => {
        return contract.signerDtos.filter(signer => signer.isSigned).length == contract.signerDtos.length;
    };

    const isSigned = contract => {
        return contract.signerDtos.filter(signer => signer.isSigned).filter(signer => signer.email == user.email).length > 0;
    };

    const getSignersSigned = contract => {
        return contract.signerDtos.filter(signer => signer.isSigned);
    };

    const RedirectToDetail = (contract, event) => {
        if (!contract.wathched) {
            onUpdateReadStatus(event, true, contract);
        }
        history.push(`/hop-dong/${contract.id}`);
    };

    const showExpired = contract => {
        const createdDate = new Date(contract.createdTime);
        const expiredDate = new Date(contract.expirationTime);
        return (expiredDate - createdDate) / 86400000 < 91;
    };
    function listContractTag(event, tag) {
        event.stopPropagation();
        const params = {
            tagId: tag.id,
        };
        Object.keys(params).forEach(key => {
            if (!params[key]) {
                delete params[key];
            }
        });
        history.push('/hop-dong-theo-the?' + new URLSearchParams(params).toString());
    }
    // START SIGN
    const onSendSign = async (value, fileName, contractSigned) => {
        return new Promise(async (resolve, reject) => {
            try {
                const data = {
                    contractId: contractSigned.id,
                    pemCert: value.pemCert,
                    signed: value.signed,
                    base64CertificateChain: value.base64CertificateChain,
                    fileName: fileName,
                };
                await ContractService.signContract(data);
                resolve(true);
            } catch (err) {
                reject(err);
            }
        });
    };

    const onDataToSign = async (data, contractSigned) => {
        return new Promise(async (resolve, reject) => {
            try {
                const obj = {
                    contractId: contractSigned.id,
                    pemCert: data.pemCert,
                    base64CertificateChain: data.base64CertificateChain,
                };
                const res = await ContractService.dataToSign(obj);
                resolve(res);
            } catch (err) {
                reject(err);
            }
        });
    };

    const signTool = async (data, contractSigned) => {
        return new Promise(async (resolve, reject) => {
            try {
                const token = await handleInfor();
                const obj = {
                    dataToSign: data.dataToSign,
                    taxCode: user.company.taxCode,
                };
                const res = await ToolUsb.signContract(token, obj);
                // dispatch(setGlobalSigning(false));
                resolve(res);
            } catch (err) {
                if (err == 'WRONG_TAX_CODE') {
                    await ContractService.updateAuthorUser(contractSigned.id);
                }
                reject(err);
            }
        });
    };

    const loopSign = (dataToSign, contractSigned) => {
        return new Promise(async (resolve, reject) => {
            let count = 0;
            let success = false;
            let error = '';
            while (count < 5) {
                try {
                    dispatch(setGlobalSigning(true));
                    const resToSign = await onDataToSign(dataToSign.data, contractSigned);
                    const resSignTool = await signTool(resToSign.data, contractSigned);
                    const resSign = await onSendSign(resSignTool.data, resToSign.data.fileName, contractSigned);
                    count = 5;
                    success = true;
                    error = '';
                } catch (err) {
                    error = err;
                    success = false;
                    if (err == 'WRONG_TAX_CODE' || err == 'CANCELLED_BY_USER') {
                        count = 5;
                        dispatch(setGlobalSigning(false));
                    } else {
                        count = count + 1;
                    }
                }
            }
            if (!success) {
                dispatch(setGlobalSigning(false));
                reject(error);
            } else {
                resolve(true);
            }
        });
    };

    const onSign = async (contractSigned, event) => {
        event.stopPropagation();
        if (!isSigning && !isGlobalSigning) {
            if ((!isSigned(contractSigned) && (contractSigned.status == 'PROCESSING' || contractSigned.status == 'WAITINGFORPARTNER'))) {
                try {
                    setIsSigning(true);
                    dispatch(setGlobalSigning(true));
                    const token = await handleInfor();
                    const dataToSign = await ToolUsb.getDataToSign(token);
                    await loopSign(dataToSign, contractSigned);
                    if (user) {
                        const signerDtos = contractSigned.signerDtos.map(item => {
                            if (item.email == user.email) {
                                return { ...item, ...{ isSigned: true } };
                            } else {
                                return item;
                            }
                        });
                    }
                    dispatch(addToast({ id: Math.floor(Math.random() * 10000), contractName: contractSigned.title, type: 'SUCCESS_SIGN_CONTRACT', color: 'green' }))
                } catch (err) {
                    switch (err) {
                        case 'WRONG_TAX_CODE':
                            dispatch(addToast({ id: Math.floor(Math.random() * 10000), contractName: contractSigned.title, color: 'purple', type: 'ERROR_AUTHOR', message: 'Sai mã số thuế.' }));
                            break;
                        case 'CANCELLED_BY_USER':
                            break;
                        case 'NOT_TOOL':
                            dispatch(setInstallToolModal({ isShow: true, userName: user.fullName }));
                            break;
                        case 'NOT_USBTOKEN':
                            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ATTACH_USBTOKEN_SIGN' }));
                            break;
                        default:
                            dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red" }));
                            break;
                    }
                } finally {
                    dispatch(setGlobalSigning(false));
                    setIsSigning(false);
                }
            }
        }
    };
    const checkTool = () => {
        return new Promise(async (resolve, reject) => {
            ToolUsb.getToken()
                .then((res) => {
                    resolve(res);
                })
                .catch((err) => {
                    reject(err);
                })
            setTimeout(() => {
                reject('NOT_TOOL');
            }, 1000);
        })
    }

    const handleInfor = () => {
        return new Promise(async (resolve, reject) => {
            try {
                // const resToken = await ToolUsb.getToken();
                const resToken = await checkTool();
                const token = resToken.data;
                const checkUSB = await ToolUsb.getCheckUsb(token);
                if (checkUSB.message.toUpperCase() == 'DISCONNECTED') {
                    reject('NOT_USBTOKEN');
                } else {
                    resolve(token);
                }
            } catch (err) {
                reject('NOT_TOOL');
            }
        });
    };
    // END SIGN
    const isSelected = () => {
        // return selectedContracts.filter(item => contract.id == item.id).length > 0;
        return checkContracts.filter(item => contract.id == item.id).length > 0;

    }

    return (
        <>
            <div style={{ pointerEvents: ((isCanceling && isSelected()) || isSigning) ? 'none' : '' }} onClick={e => RedirectToDetail(contract, e)} className={`contract_item ${contract.wathched ? '' : 'ci_unread'} row mx-0 ${contract.status == ContractStatus.CANCEL ? 'ci_cancel' : ''}`} key={`contract_${index}`}>
                {(isSigning || (isCanceling && isSelected())) && <div className="item-contract-loader"> <div className="loader-content"><Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> {isSigning ? 'Đang ký...' : 'Đang hủy...'} </div></div>}
                {showAction && (
                    <>
                        <div className="checkbox col-auto px-2 order-0 order-lg-0">
                            <div onClick={e => e.stopPropagation()} className="custom-control custom-checkbox d-inline-block">
                                <input type="checkbox" className="custom-control-input" id={`check${index}`} checked={checkContracts.filter(item => item.id == contract.id).length > 0} onChange={e => onCheck(e, contract, e.target.checked)} name="checkbox-all" />
                                <label className="custom-control-label" htmlFor={`check${index}`}></label>
                            </div>
                            {contract.userCreate.id == user.id ? <IconReceive /> : <IconSend />}
                        </div>
                    </>
                )}
                {showAvatar && (
                    <>
                        <div className="ci_img go_cell col-auto pl-2 pr-3 order-0 d-none d-xl-block">
                            <div className="ci_img_wrap">
                                <img
                                    src={contract.userCreate.avatarPath ? contract.userCreate.avatarPath : ''}
                                    className="rounded-circle"
                                    onError={e => {
                                        e.target.onerror = null;
                                        e.target.remove();
                                    }}
                                    alt=""
                                    loading="lazy"
                                />
                                <div className="img_alt">
                                    <span>{GeneralService.getLetterName(contract.userCreate.fullName)}</span>
                                </div>
                            </div>
                        </div>
                    </>
                )}
                <div className="ci_people go_cell col pl-2 pl-xl-0 pr-0 order-4 order-sm-1 order-lg-1">
                    <div>
                        {contract.userCreate.fullName}, ({contract.signerDtos.length - 1})
                    </div>
                    <div className="sign_status d-none d-lg-block">
                        {showContractType && (contract.type === 'CREATER' ? <IconReceive /> : <IconSend />)}
                        {!showAction && <> &nbsp;</>}
                        <span onClick={e => onSign(contract, e)} className={`ss_icon ${isSigned(contract) ? 'completed' : ''}`} style={{ cursor: (!isSigned(contract) && (contract.status == 'PROCESSING' || contract.status == 'WAITINGFORPARTNER')) ? "" : 'not-allowed' }}>
                            <IconPencil />
                        </span>
                        <span className={isContractCompleted(contract) ? 'completed' : ''}>
                            {getSignersSigned(contract).length}/{contract.signerDtos.length}
                        </span>
                    </div>
                    <div className="people_list shadow d-none d-md-block">
                        {contract.signerDtos.map((partner, i) => (
                            <React.Fragment key={i}>
                                {/* {partner.email != contract.userCreate.email && ( */}
                                <div className="row person_item">
                                    <div className="col-auto pr-0">{partner.avatarPath ? <img src={partner.avatarPath ? partner.avatarPath : ''} className="rounded-circle person_img" alt="" loading="lazy" /> : <div className="person_img_alt">{letterNameChar(partner.fullName)}</div>}</div>
                                    <div className="col pr-0 person_text">
                                        <div className="person_name">{partner.fullName}</div>
                                        <div className="person_email text-secondary">{partner.email}</div>
                                    </div>
                                    {partner.isSigned ? (
                                        <div className="col-auto ml-auto completed d-flex align-items-center">
                                            <div className="">
                                                {' '}
                                                <IconPencil />
                                            </div>
                                        </div>
                                    ) : (
                                        ''
                                    )}
                                </div>
                                {/* )} */}
                            </React.Fragment>
                        ))}
                    </div>
                </div>
                <div className="ci_title go_cell col order-4 order-lg-2 pl-2 pl-lg-3 pr-2 pr-lg-3">
                    <div className="ci_name">
                        <span>{contract.title}</span> - {contract.content}
                    </div>
                    <div className="attachment align-items-center">
                        <IconFile />
                        <span className="attachment_name">{contract.fileName}</span>
                    </div>
                </div>
                <div className={`ci_responses ${contract.wathched ? '' : 'ci_response_unread'} go_cell col-auto pl-0 order-2 order-lg-3 ml-auto`}>
                    <div className="text-center">
                        <div className="ci_responses_count">{contract.countCommnet}</div>
                        <div className="ci_responses_text">PHẢN HỒI</div>
                    </div>
                </div>
                <div className="ci_status go_cell col-auto text-center pl-0 pr-2 order-5 order-lg-4 ml-auto ml-lg-0">
                    <div className={`status_text ${contractStatusClassName(contract.status)}`}>{contractStatusDesc(contract.status)}</div>
                    {showExpired(contract) && <div className={`${contract.status == ContractStatus.EXPIRED ? 'text-danger' : ''}`}>{dateToString(contract.expirationTime)}</div>}
                </div>

                {showAction && (
                    <>
                        <div className="ci_action col-auto text-right pr-2 order-6">
                            {contract.wathched ? (
                                <OverlayTrigger overlay={<Tooltip>Đánh dấu là chưa đọc</Tooltip>} placement="top">
                                    {({ ref, ...triggerHandler }) => (
                                        <a href="javascript:void(0)" {...triggerHandler} onClick={e => onUpdateReadStatus(e, false, contract)} ref={ref}>
                                            <IconUnread />
                                        </a>
                                    )}
                                </OverlayTrigger>
                            ) : (
                                <OverlayTrigger overlay={<Tooltip>Đánh dấu là đã đọc</Tooltip>} placement="top">
                                    {({ ref, ...triggerHandler }) => (
                                        <a href="javascript:void(0)" {...triggerHandler} onClick={e => onUpdateReadStatus(e, true, contract)} ref={ref}>
                                            <IconRead />
                                        </a>
                                    )}
                                </OverlayTrigger>
                            )}

                            <OverlayTrigger overlay={<Tooltip> Tải hợp đồng </Tooltip>} placement="top">
                                {({ ref, ...triggerHandler }) => (
                                    <a href="javascript:void(0)" {...triggerHandler} onClick={e => onDownloadContracts(e, contract)} ref={ref}>
                                        <IconDownload />
                                    </a>
                                )}
                            </OverlayTrigger>
                            {
                                ((contract.status == 'WAITINGFORPARTNER' && !isSigned(contract)) || contract.status == "PROCESSING") &&
                                <OverlayTrigger overlay={<Tooltip> Hủy hợp đồng </Tooltip>} placement="top">
                                    {({ ref, ...triggerHandler }) => (
                                        <a href="javascript:void(0)" {...triggerHandler} onClick={e => onCancelContracts(e, contract, true)} ref={ref}>
                                            <IconDelete />
                                        </a>
                                    )}
                                </OverlayTrigger>
                            }

                            <OverlayTrigger overlay={<Tooltip>Danh sách thẻ</Tooltip>} placement="top">
                                {({ ref, ...triggerHandler }) => (
                                    <>
                                        <span onClick={e => onAttachTagContracts(e, contract, true)}>
                                            <a href="javascript:void(0)" {...triggerHandler} ref={ref}>
                                                <IconTag />
                                            </a>
                                        </span>
                                    </>
                                )}
                            </OverlayTrigger>
                        </div>
                        <div className="ci_action_mobi col-auto order-3 text-center ml-auto mb-1 pl-1 pr-2">
                            <Dropdown>
                                <Dropdown.Toggle as={CustomToggle}>
                                    <IconDot />
                                </Dropdown.Toggle>
                                <Dropdown.Menu popperConfig={{ placement: 'right-end' }}>
                                    {contract.wathched ? (
                                        <Dropdown.Item href="javascript:void(0)" onClick={e => onUpdateReadStatus(e, false, contract)}>
                                            <IconUnread />
                                            &ensp;&nbsp;Đánh dấu là chưa đọc
                                        </Dropdown.Item>
                                    ) : (
                                        <Dropdown.Item href="javascript:void(0)" onClick={e => onUpdateReadStatus(e, true, contract)}>
                                            <IconRead />
                                            &ensp;&nbsp;Đánh dấu là đã đọc
                                        </Dropdown.Item>
                                    )}
                                    <Dropdown.Item href="javascript:void(0)" onClick={e => onDownloadContracts(e, contract)}>
                                        <IconDownload />
                                        &ensp;&nbsp;Tải hợp đồng
                                    </Dropdown.Item>
                                    {
                                        ((contract.status == 'WAITINGFORPARTNER' && !isSigned(contract)) || contract.status == "PROCESSING") &&
                                        <Dropdown.Item href="javascript:void(0)" onClick={e => onCancelContracts(e, contract, true)}>
                                            <IconDownload />
                                            &ensp;&nbsp;Hủy hợp đồng
                                        </Dropdown.Item>
                                    }
                                    <Dropdown.Item href="javascript:void(0)" onClick={e => onAttachTagContracts(e, contract, true)} data-toggle="modal" data-target="#tagsModal">
                                        <IconTag />
                                        &ensp;&nbsp;Danh sách thẻ
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                        <div className="sticky_tags">
                            {contract.tagDtos &&
                                contract.tagDtos.map((tag, index) => (
                                    <>
                                        {index < 3 && (
                                            <div style={{ background: `${tag.colorCode}`, cursor: 'pointer' }} onClick={e => listContractTag(e, tag)} className="st_item" key={index}>
                                                <span>{tag.name}</span>
                                            </div>
                                        )}
                                    </>
                                ))}
                        </div>
                    </>
                )}
            </div>
        </>
    )
}
export default ContractItem;