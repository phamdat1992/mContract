import { Comments, ContractDetailInformation, ContractDetailPDF, ContractDetailPreview, ContractDetailNotifications, SignersInformation } from '@Containers/Contract/ContractDetail';
import { useEffect, useState } from 'react';
import { Nav, NavItem, NavLink, TabContainer, TabContent, TabPane } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { IconChatRightText, IconInfoCircle, IconPeople } from '@Components/Icon';
import ContractService from '@Services/contract';
import * as Axios from 'axios';
import ToolUsb from '@Services/toolUsb';
import { Spinner } from 'react-bootstrap';
import { setContract, updateContract, setCurrentSigner, setCurrentSignerToken, addNewNotification } from '@Redux/Actions/DetailContract';
import SimpleBar from 'simplebar-react';
import GeneralService from '@Services/general';
import ContractStatus from '@Enums/contract-status';
import { useHistory } from 'react-router-dom';
import { setFocusComment, setPdfCurrentPage, setPreviewCurrentPage, setFocusSign } from '@Redux/Actions/DetailContract';
import { setInstallToolModal } from '@Redux/Actions/InstallToolModal';
import { addToast } from '@Redux/Actions/AlertToast';
import { useLocation } from 'react-router-dom';
PageContractPublic.defaultProps = {
  stylePeople: 'px-3 row py-2',
  styleComment: 'row px-3 pt-2',
};

function PageContractPublic(props) {
  const location = useLocation();
  const { contract, comments, currentSigner, newComment, focusComment, currentSignerToken, isCanceling, isLoadedAllPageDetail, isLoadedAllPagePreviewDetail } = useSelector(state => state.detailContract);
  const dispatch = useDispatch();
  const history = useHistory();
  const [isRequesting, setIsRequesting] = useState(false);
  const [keyTab, setTabKey] = useState('detail');

  const getFile = () => {
    return new Promise((resolve, reject) => {
      Axios.get(contract.fileUrl, { responseType: 'blob' })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          console.error(err);
        });
    });
  };

  const onSendSign = (value, fileName) => {
    return new Promise(async (resolve, reject) => {
      try {
        const data = {
          pemCert: value.pemCert,
          signed: value.signed,
          base64CertificateChain: value.base64CertificateChain,
          fileName: fileName,
        };
        await ContractService.signContractSigner(currentSignerToken, data);
        resolve(true);
      } catch (err) {
        reject(err);
      }
    });
  };

  const moveSignaturePos = () => {
    const signPos = document.getElementById(`signDetail_${currentSigner.email}`);
    if (signPos) {
      signPos.scrollIntoView({ block: 'center' });
      contract.signerDtos.forEach(item => {
        if (item.email == currentSigner.email) {
          dispatch(setFocusSign(item));
        }
      })
    }
    setTabKey('people');
    const inforSigner = document.querySelector(`.inforSigner_${currentSigner.id}`);
    if (inforSigner) {
      inforSigner.scrollIntoView({
        block: 'center',
      });
    }
  };
  const onDataToSign = async data => {
    return new Promise(async (resolve, reject) => {
      try {
        const obj = {
          contractId: contract.id,
          pemCert: data.pemCert,
          base64CertificateChain: data.base64CertificateChain,
        };
        const res = await ContractService.dataToSignSigner(currentSignerToken, obj);
        resolve(res);
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      }
    });
  };
  const signTool = async data => {
    return new Promise(async (resolve, reject) => {
      try {
        const token = await handleInfor();
        const obj = {
          dataToSign: data.dataToSign,
          taxCode: currentSigner.taxCode,
        };
        const res = await ToolUsb.signContract(token, obj);
        resolve(res);
      } catch (err) {
        if (err == 'WRONG_TAX_CODE') {
          await ContractService.updateAuthorSigner(currentSignerToken);
        }
        reject(err);
      }
    });
  };

  const loopSign = dataToSign => {
    return new Promise(async (resolve, reject) => {
      let count = 0;
      let success = false;
      let error = '';
      while (count < 5) {
        try {
          const resToSign = await onDataToSign(dataToSign.data);
          const resSignTool = await signTool(resToSign.data);
          const resSign = await onSendSign(resSignTool.data, resToSign.data.fileName);
          count = 5;
          success = true;
          error = '';
        } catch (err) {
          error = err;
          success = false;
          if (err == 'WRONG_TAX_CODE' || err == 'CANCELLED_BY_USER') {
            count = 5;
          } else {
            count = count + 1;
          }
        }
      }
      if (!success) {
        reject(error);
      } else {
        resolve(true);
      }
    });
  };

  const onSign = async () => {
    try {
      moveSignaturePos();
      setIsRequesting(true);
      const token = await handleInfor();
      const dataToSign = await ToolUsb.getDataToSign(token);
      await loopSign(dataToSign);
      const signerDtos = contract.signerDtos.map(item => {
        if (item.email == currentSigner.email) {
          return { ...item, ...{ isSigned: true } };
        } else {
          return item;
        }
      });
      dispatch(updateContract({ ...contract, ...{ signerDtos: signerDtos } }));
      dispatch(setCurrentSigner({ ...currentSigner, ...{ isSigned: true, signedTime: new Date() } }));
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), contractName: contract.title, type: 'SUCCESS_SIGN_CONTRACT', color: 'green' }))
    } catch (err) {
      switch (err) {
        case 'WRONG_TAX_CODE':
          dispatch(updateContract({ ...contract, ...{ status: 'AUTHENTICATIONFAIL' } }));
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), contractName: contract.title, color: 'purple', type: 'ERROR_AUTHOR', message: 'Sai mã số thuế.' }));
          break;
        case 'CANCELLED_BY_USER':
          break;
        case 'NOT_TOOL':
          dispatch(setInstallToolModal({ isShow: true, userName: currentSigner.fullName }));
          break;
        case 'NOT_USBTOKEN':
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ATTACH_USBTOKEN_SIGN' }));
          break;
        default:
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: 'ERROR', color: "red" }));
          break;
      }
    } finally {
      setIsRequesting(false);
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

  const getSigner = async token => {
    try {
      const res = await ContractService.inforSigner(token);
      dispatch(setCurrentSigner(res.data));
    } catch (err) {
      console.error(err);
    }
  };
  const getNotificationsUnread = async () => {
    try {
      const token = props.match.params.slug;
      const res = await ContractService.notificationsUnreadSigner(token, { isRead: false });
      res.data.forEach((notify, index) => {
        const notifyData = {
          id: notify.commentId,
          content: notify.commentContent,
          parentId: notify.commentParentId ? notify.commentParentId : null,
          x: notify.commentX || null,
          y: notify.commentY || null,
          page: notify.commentPage || null,
          signer: notify.signerId
            ? {
              id: notify.signerId ? notify.signerId : null,
              fullName: notify.signerFullName ? notify.signerFullName : '',
              email: notify.signerEmail ? notify.signerEmail : '',
              avatarPath: notify.userAvatar ? notify.userAvatar : '',
            }
            : null,
          user: notify.userId
            ? {
              id: notify.userId ? notify.userId : null,
              fullName: notify.userName || '',
              email: notify.userEmail || '',
              avatarPath: notify.userAvatar ? notify.userAvatar : '',
            }
            : null,
          childCommentDtos: [],
          createdTime: new Date(notify.commentCreatedTime).toISOString() || new Date(notify.createdTime).toISOString(),
          // watched: false,
          isRead: false,
        };
        const notification = {
          id: notify.id,
          type: notify.type,
          createdTime: new Date(notify.createdTime).toISOString(),
          contractId: notify.contractId,
          contractName: notify.contractName,
          contractContent: notify.contractContent,
          data: notifyData,
          status: notify.status,
        };
        // Start scroll tới comment mới nhất
        if (index == 0 && !focusComment && !(location.search.indexOf('signerId') > -1 || location.search.indexOf('commentId') > -1)) {
          dispatch(setFocusComment(notifyData));
          dispatch(setPdfCurrentPage(notifyData.page));
          dispatch(setPreviewCurrentPage(notifyData.page));
        }
        // End
        dispatch(addNewNotification(notification));
      });
    } catch (err) {
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
    }
  };
  const getContracts = async () => {
    try {
      const token = props.match.params.slug;
      dispatch(setCurrentSignerToken(token));
      const res = await ContractService.detailContractSigner(token);
      dispatch(setContract(res.data));
      getSigner(token);
      // if (location.search && location.search.indexOf('signerId') > -1) {
      //   res.data.signerDtos.forEach(item => {
      //     if (item.id == (location.search.split('='))[1]) {
      //       dispatch(setFocusSign(item));
      //     }
      //   })
      // }
    } catch (err) {
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      history.push('/trang-chu');
    }
  };

  const isHasSigned = () => {
    return (currentSigner && currentSigner.isSigned) || contract.status == ContractStatus.CANCEL || contract.status == ContractStatus.EXPIRED || contract.status == ContractStatus.COMPLETE || contract.status == ContractStatus.AUTHENTICATIONFAIL;
  };
  const countComments = () => {
    let sum = 0;
    if (comments && comments.length > 0) {
      comments.forEach(comment => {
        sum += 1;
        if (comment.childCommentDtos && comment.childCommentDtos.length > 0) {
          sum += comment.childCommentDtos.length;
        }
      });
    }
    return sum;
  };

  const moveSingFromLink = () => {
    if (location.search) {
      const arr = location.search.split('=');
      if (arr.length > 0 && location.search.indexOf('signerId') > -1) {
        setTimeout(() => {
          if (contract) {
            contract.signerDtos.forEach(item => {
              if (item.id == arr[1]) {
                dispatch(setFocusSign(item))
              }
            })
          }
        }, 100)
      }
    }
  }

  useEffect(() => {
    document.title = 'MContract';
  }, []);

  useEffect(() => {
    if (newComment) {
      setTabKey('comments');
    }
  }, [newComment]);

  useEffect(() => {
    if (focusComment && focusComment.id) {
      setTabKey('comments');
    }
  }, [focusComment]);
  useEffect(() => {
    moveSingFromLink();
  }, [contract]);
  useEffect(() => {
    getContracts();
    getNotificationsUnread();
    if (location.search) {
      if (location.search.indexOf('commentId') > -1) {
        setTabKey('comments')
      }
      if (location.search.indexOf('signerId') > -1) {
        setTabKey('people')
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      {contract && currentSigner ? (
        <>
          <div className={`docsign ${isHasSigned() ? 'has-signed' : ''} ${isCanceling ? 'pointer-canceling' : ''}`} >
            <header className="docsign_header">
              <div className="top_border"></div>
              <div className="container-fluid header_text">
                <div className="row">
                  <div className="col px-5 d-flex align-items-center">
                    <div className="d-none d-md-block company_name w-100 text-center pl-2">{contract.company.name}</div>
                  </div>
                  <div className="col px-5 d-flex align-items-center">
                    <div className="d-none d-md-block w-100 text-center pr-2">
                      <span className="d-inline-block px-2">Điện thoại: {contract.company.phoneNumber ? contract.company.phoneNumber : contract.userCreate.phoneNumber}</span>
                      <span className="d-inline-block px-2">Email: {contract.company.email ? contract.company.email : contract.userCreate.email}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div className="logo_wrap shadow-sm">
                <img
                  src={contract.company.logoPath ? contract.company.logoPath : ''}
                  onError={e => {
                    e.target.onerror = null;
                    e.target.remove();
                  }}
                  alt=""
                />
                <div className="img_alt">
                  <span>{GeneralService.getLetterName(contract.company.name)}</span>
                </div>
              </div>
            </header>

            <main className="docsign_main mt-4">
              <div className="main_wrap container-fluid">
                <div className="row contract_name">
                  <h1 className="w-100">{contract.title}</h1>
                </div>
                <div className="container-fluid p-0">
                  <div className="row">
                    <ContractDetailPreview setTabKey={setTabKey.bind(this)}></ContractDetailPreview>
                    <div className="col doc_preview p-0">
                      <ContractDetailPDF getContracts={getContracts} setTabKey={setTabKey.bind(this)} />
                    </div>
                    <div className="d-none d-lg-block col-auto info_tabs p-0">
                      <TabContainer
                        defaultActiveKey="detail"
                        activeKey={keyTab}
                        onSelect={k => {
                          setTabKey(k);
                        }}
                      >
                        <Nav variant="pills" className="nav-fill nav-justified" role="tablist">
                          <NavItem>
                            <NavLink eventKey="detail" title="Thông tin" id="detail-tab" role="tab" aria-controls="home">
                              <IconInfoCircle />
                            </NavLink>
                          </NavItem>
                          <NavItem>
                            <NavLink eventKey="people" title="Danh sách người nhận" id="people-tab" role="tab" aria-controls="profile">
                              <IconPeople />
                              &nbsp;
                              <span>({contract.signerDtos.length})</span>
                            </NavLink>
                          </NavItem>
                          <NavItem>
                            <NavLink eventKey="comments" title="Nhận xét" id="comments-tab" role="tab" aria-controls="contact">
                              <IconChatRightText />
                              &nbsp;
                              <span>({countComments()})</span>
                            </NavLink>
                          </NavItem>
                        </Nav>
                        <SimpleBar id="docsignTabContent">
                          <TabContent>
                            <TabPane eventKey="detail" className="p-3" aria-labelledby="home-tab">
                              <ContractDetailInformation />
                            </TabPane>
                            <TabPane eventKey="people" aria-labelledby="profile-tab">
                              <SignersInformation />
                            </TabPane>
                            <TabPane eventKey="comments" className="pb-2" aria-labelledby="contact-tab">
                              <Comments setTabKey={setTabKey.bind(this)} />
                            </TabPane>
                          </TabContent>
                        </SimpleBar>
                      </TabContainer>
                    </div>
                  </div>
                </div>
              </div>
            </main>
            <div className="float_noti">
              <ContractDetailNotifications setTabKey={setTabKey.bind(this)} />
            </div>
            <footer>
              {!isHasSigned() && (
                <div className="sign_btn text-center">
                  <button disabled={isRequesting} className="btn btn_site my-2" onClick={() => onSign()}>
                    {isRequesting ? (
                      <>
                        <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                      </>
                    ) : (
                      <>Ký Hợp đồng</>
                    )}
                  </button>
                </div>
              )}
              <div className="footer_text d-flex align-items-center">
                <div className="w-100 text-center">
                  <span className="d-none d-md-inline">Hệ thống ký hợp đồng điện tử được phát triển bởi</span> Công ty TNHH Dịch Vụ Công Nghệ Trực Tuyến
                </div>
              </div>
            </footer>
          </div>
        </>
      ) : (
        <></>
      )
      }
    </>
  );
}

export { PageContractPublic };
