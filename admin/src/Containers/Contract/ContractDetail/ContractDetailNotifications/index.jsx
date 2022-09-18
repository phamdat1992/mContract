/* eslint-disable react-hooks/exhaustive-deps */
import React, { useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import Socket from '@Utils/socket';
import { get } from '@Utils/cookie';
import { addNewNotification, removeNotification, addComment, addChildComment, setComments, resetData, updateComments, setShowSignerModal, setFocusSign } from '@Redux/Actions/DetailContract';
import { IconPencil, IconFileText, IconChatRightText } from '@Components/Icon';
import { actionDesc, letterNameChar, timeDesc } from '@Utils/helpers';
import { setPdfCurrentPage, setPreviewCurrentPage } from '@Redux/Actions/DetailContract';
import { setFocusComment, setFocusRealtimeComment } from '@Redux/Actions/DetailContract';
import ContractService from '@Services/contract';
import CommentService from '@Services/comment';
import { useLocation } from 'react-router-dom';
import { updateContract, setCurrentSigner, setNotificationsUnread } from '@Redux/Actions/DetailContract';
import { SOCKET_ACTION } from '@Enums/socket';
import { addToast } from '@Redux/Actions/AlertToast';
import { setIsDisconnect } from '@Redux/Actions/Data';

const NotificationIcon = ({ notify }) => {
  switch (notify.type) {
    case 'COMMENT':
      if (notify.data.parentId) {
        return <IconChatRightText />;
      } else {
        return <IconFileText />;
      }
    default:
      return <IconPencil />;
  }
};

const NotificationTime = ({ notify }) => {
  const [time, setTime] = useState(timeDesc(new Date(notify.createdTime)));
  let timeout;

  useEffect(() => {
    const today = new Date();
    const createdDate = new Date(notify.createdTime);

    if (today.getDate() != createdDate.getDate()) {
      const d = new Date(notify.createdTime);
      setTime(('0' + d.getDate()).slice(-2) + '/' + ('0' + (d.getMonth() + 1)).slice(-2) + '/' + d.getFullYear());
    } else {
      timeout = setInterval(() => {
        setTime(timeDesc(new Date(notify.createdTime)));
      }, 1000);
      return () => {
        clearInterval(timeout);
      };
    }
  }, []);
  return <>{time}</>;
};

const CommentNotification = ({ notify, onHide, setTabKey }) => {
  const { user } = useSelector(state => state.auth);
  const location = useLocation();
  const {
    contract,
    currentSignerToken,
    isShowSignerModal,
    focusSign,
    pdf: { currentPage },
  } = useSelector(state => state.detailContract);
  const dispatch = useDispatch();
  const markedNoti = async notify => {
    return new Promise(async (resolve, reject) => {
      try {
        if (user) {
          await ContractService.markNotiContractUser(contract.id, notify.id);
          if (notify.type == 'COMMENT') {
            await CommentService.markCommentUser(notify.data.id);
            dispatch(updateComments({ id: notify.data.id }));
            if (location.search && location.search.indexOf('signerId') > -1) {
              setTabKey('comments');
            }
          }
        } else {
          await ContractService.markNotiContractSigner(currentSignerToken, notify.id);
          if (notify.type == 'COMMENT') {
            await CommentService.markCommentSigner(currentSignerToken, notify.data.id);
            dispatch(updateComments({ id: notify.data.id }));
            if (location.search && location.search.indexOf('signerId') > -1) {
              setTabKey('comments');
            }
          }
        }
        resolve(true);
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
      }
    });
  };

  const moveCommentPos = comment => {
    if (comment && comment.id) {
      const signPos = document.getElementById(`comment_${comment.parentId ? comment.parentId : comment.id}`);
      if (signPos) {
        signPos.scrollIntoView({
          block: 'center',
        });
      }
    }
  };

  const moveSignPos = sign => {
    if (sign) {
      const signPos = document.getElementById(`signDetail_${sign.user ? sign.user.email : sign.signer.email}`);
      if (signPos) {
        signPos.scrollIntoView({
          block: 'center',
        });
      }
      setTabKey('people');
      if (window.visualViewport.width < 992) {
        if (!isShowSignerModal && window.visualViewport.width < 992) {
          dispatch(setShowSignerModal(true));
          setTimeout(() => {
            const modalSigners = document.getElementById('mobileSigners');
            if (modalSigners) {
              const inforSigner = modalSigners.querySelector(`.inforSigner_${sign.user ? sign.user.id : sign.signer.id}`);
              if (inforSigner) {
                inforSigner.scrollIntoView({
                  block: 'center',
                });
              }
            }
          }, 100);
        } else {
          const inforSigner = document.querySelector(`.inforSigner_${sign.user ? sign.user.id : sign.signer.id}`);
          if (inforSigner) {
            inforSigner.scrollIntoView({
              block: 'center',
            });
          }
        }
      }
    }
  };

  async function onMove(e, notify) {
    if (notify.type == 'COMMENT') {
      dispatch(setFocusComment(notify.data));
      moveCommentPos(notify.data);
      await markedNoti(notify);
      onHide(notify);
    } else {
      moveSignPos(notify.data);
      await markedNoti(notify);
      onHide(notify);
    }
  }

  return (
    <>
      {!notify.status && (
        <a onClick={e => onMove(e, notify)} style={{ cursor: 'pointer' }} id={notify.id} className="noti_item unread shadow">
          <div onClick={e => onMove(e, notify)} className="fn_close">
            &times;
          </div>
          <div className="row mx-0 px-1">
            <div className="col-auto pr-0 pl-2">
              <div className="person_img_wrap">
                {(notify.data.user && notify.data.user.avatarPath) || (notify.data.signer && notify.data.signer.avatarPath) ? (
                  <img
                    src={notify.data.user && notify.data.user.avatarPath ? notify.data.user.avatarPath : notify.data.signer.avatarPath}
                    className="rounded-circle person_img"
                    alt=""
                    loading="lazy"
                    onError={e => {
                      e.target.onerror = null;
                      e.target.remove();
                    }}
                    alt=""
                  />
                ) : (
                  <div className="img_alt">
                    <span>{letterNameChar(notify.data.user ? notify.data.user.fullName : notify.data.signer.fullName)}</span>
                  </div>
                )}
              </div>
              <div className="noti_sicon">
                <NotificationIcon key={`notification_detail_icon_${notify.id}`} notify={notify} />
              </div>
            </div>
            <div className="col noti_text">
              <div>
                <div className="noti_name">
                  {notify.data.user ? notify.data.user.fullName : notify.data.signer.fullName} - {notify.contractName}
                </div>
                <div className="noti_content"> {notify.type == 'COMMENT' ? notify.data.content : notify.contractContent}</div>
              </div>
              <div className="noti_meta">
                <span>{actionDesc(notify)}:</span>{' '}
                <span className="time">
                  <NotificationTime key={`notification_detail_time_${notify.id}`} notify={notify} />
                </span>
              </div>
            </div>
          </div>
        </a>
      )}
    </>
  );
};

const SingleNotification = ({ notify, onHide, setTabKey }) => {
  switch (notify.type) {
    case 'COMMENT':
      return <CommentNotification setTabKey={setTabKey} key={`notification_detail_icon_${notify.id}`} notify={notify} onHide={onHide} />;
    case 'SIGN':
      return <CommentNotification setTabKey={setTabKey} key={`notification_detail_icon_${notify.id}`} notify={notify} onHide={onHide} />;
    default:
      return <></>;
  }
};

const ContractDetailNotifications = ({ setTabKey }) => {
  const {
    notifications,
    currentSignerToken,
    contract,
    currentSigner,
    focusSign,
    focusComment,
    pdf: { currentPage },
  } = useSelector(state => state.detailContract);
  const contractRef = useRef(contract);
  const location = useLocation();
  const dispatch = useDispatch();
  const currentSignerRef = useRef(currentSigner);
  const user = useSelector(state => state.auth.user);

  function onHide(notify) {
    const dom = document.getElementById(notify.id);
    if (dom) {
      dom.classList.add('fadeOut');
      setTimeout(() => {
        dispatch(removeNotification(notify.id));
        dom.classList.remove('fadeOut');
      }, 350);
    }
  }
  const convertComments = comments => {
    return comments.map(cmt => {
      if (cmt.childCommentDtos) {
        return {
          ...cmt,
          ...{ childCommentDtos: convertComments(cmt.childCommentDtos) },
        };
      } else {
        return {
          ...cmt,
          ...{ childCommentDtos: [] },
        };
      }
    });
  };

  const getComments = () => {
    if (user) {
      CommentService.list(contract.id)
        .then(res => {
          dispatch(setComments(convertComments(res.data)));
        })
        .catch(err => {});
    } else {
      CommentService.listCommentSigner(currentSignerToken)
        .then(res => {
          dispatch(setComments(convertComments(res.data)));
        })
        .catch(err => {});
    }
  };

  const getNotificationsUnread = async () => {
    try {
      const contractId = contract.id;
      const res = user ? await ContractService.notificationsUnreadUser(contractId, { isRead: false }) : await ContractService.notificationsUnreadSigner(currentSignerToken, { isRead: false });
      let isNewest = false;
      dispatch(setNotificationsUnread([]));
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
        // Start scroll tới comment mới nhất chưa đọc
        if (!isNewest && !focusComment && notify.type == 'COMMENT' && !(location.search.indexOf('signerId') > -1 || location.search.indexOf('commentId') > -1)) {
          isNewest = true;
          dispatch(setFocusComment(notifyData));
          dispatch(setPdfCurrentPage(notifyData.page));
          dispatch(setPreviewCurrentPage(notifyData.page));
        }
        // End scroll tới comment mới nhất chưa đọc
        dispatch(addNewNotification(notification));
      });
    } catch (err) {
      // dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
    }
  };

  useEffect(() => {
    contractRef.current = contract;
  }, [contract]);

  useEffect(async () => {
    const token = currentSignerToken ? currentSignerToken : get('digital_signature_token');
    Socket.startConnect(token, () => {
      Socket.connection.emit('notify', {
        type: SOCKET_ACTION.JOIN_CONTRACT_DETAIL,
        contractId: contract.id,
      });
      Socket.connection.on('disconnect', () => {
        // dispatch(setIsDisconnect(true));
        getComments();
        getNotificationsUnread();
      });
    });
    Socket.onNotify(notify => {
      if (notify.contractId == contract.id && ((notify.signerId && notify.signerId != currentSignerRef.current.id) || (notify.userId && notify.userId != currentSignerRef.current.id))) {
        let notifyData = null;
        if ((notify.type == 'COMMENT' || notify.type == 'SIGN') && !notify.isDashboard) {
          if (notifications.filter(item => notify.id == item.id).length == 0) {
            switch (notify.type) {
              case 'COMMENT':
                notifyData = {
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
                        avatarPath: notify.userAvatar,
                      }
                    : null,
                  user: notify.userId
                    ? {
                        id: notify.userId ? notify.userId : null,
                        fullName: notify.userName || '',
                        email: notify.userEmail || '',
                        avatarPath: notify.userAvatar,
                      }
                    : null,
                  childCommentDtos: [],
                  createdTime: new Date(notify.commentCreatedTime).toISOString() || new Date(notify.createdTime).toISOString(),
                  isRead: false,
                };
                break;
              case 'SIGN':
                notifyData = {
                  id: notify.signerId,
                  signer: notify.signerId
                    ? {
                        id: notify.signerId ? notify.signerId : null,
                        fullName: notify.signerFullName ? notify.signerFullName : '',
                        email: notify.signerEmail ? notify.signerEmail : '',
                        avatarPath: notify.userAvatar,
                      }
                    : null,
                  user: notify.userId
                    ? {
                        id: notify.userId ? notify.userId : null,
                        fullName: notify.userName || '',
                        email: notify.userEmail || '',
                        avatarPath: notify.userAvatar,
                      }
                    : null,
                  createdTime: new Date(notify.createdTime).toISOString(),
                  isRead: false,
                };
                break;
              default:
                notifyData = {};
            }

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
            dispatch(addNewNotification(notification));
            if (notification.type == 'COMMENT') {
              const cmt = { ...notification.data, ...{ latestRealtime: true } };
              dispatch(setFocusComment(cmt));
              // dispatch(setFocusRealtimeComment(cmt));
              if (notification.data.parentId) {
                dispatch(addChildComment(cmt));
              } else {
                dispatch(addComment(cmt));
              }
              if (location.search && location.search.indexOf('signerId') > -1) {
                setTabKey('comments');
              }
            }
            if (notify.type == 'SIGN') {
              const signerDtos = contractRef.current.signerDtos.map(item => {
                if (item.email == (notify.userEmail ? notify.userEmail : notify.signerEmail)) {
                  return { ...item, ...{ isSigned: true } };
                } else {
                  return item;
                }
              });
              if (currentSigner.email == (notify.userEmail ? notify.userEmail : notify.signerEmail)) {
                dispatch(setCurrentSigner({ ...currentSigner, ...{ isSigned: true, signedTime: new Date() } }));
              }
              dispatch(updateContract({ ...contract, ...{ signerDtos: signerDtos } }));
            }
            
          }
        } else if (notify.type == 'CANCEL' || notify.type == 'EXPIRED' || notify.type == 'EXPIRED' || notify.type == 'AUTHENTICATIONFAIL') {
          dispatch(updateContract({ ...contract, ...{ status: notify.type } }));
        }
      }
    });
  }, []);

  useEffect(() => {
    return () => {
      console.log(Socket.connection);
      Socket.connection.removeListener('notify');
      Socket.connection.removeListener('disconnect');
      Socket.connection.emit('notify', {
        type: SOCKET_ACTION.LEAVE_CONTRACT_DETAIL,
        contractId: contract.id,
      });
      dispatch(resetData());
    };
  }, []);

  function onMoveComment(comment) {
    if (comment && comment.id) {
      const signPos = document.getElementById(`comment_${comment.id}`);
      if (signPos) {
        signPos.scrollIntoView({
          block: 'center',
        });
      }
    }
    if (comment.page != currentPage) {
      dispatch(setPdfCurrentPage(comment.page));
      dispatch(setPreviewCurrentPage(comment.page));
    }
  }
  useEffect(() => {
    currentSignerRef.current = currentSigner;
  }, [currentSigner]);
  return (
    <>
      {notifications
        .filter(n => n.type && !n.status)
        .map((n, index) => (
          <React.Fragment key={`notification_detail_${n.id}`}>
            {window.innerHeight < window.innerWidth && window.innerHeight < 500 ? <>{index < 3 && <SingleNotification notify={n} onHide={onHide} setTabKey={setTabKey} />}</> : <>{index < 5 && <SingleNotification notify={n} onHide={onHide} setTabKey={setTabKey} />}</>}
          </React.Fragment>
        ))}
    </>
  );
};
export default ContractDetailNotifications;
