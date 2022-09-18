import { setCommentNotifications, setExpireNotifications, setNewContractNotifications } from '@Redux/Actions/Notification';
import { get } from '@Utils/cookie';
import Socket from '@Utils/socket';
import React, { useEffect, useState, useRef } from 'react';
import { Dropdown } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import NotificationComment from './NotificationComment';
import NotificationExpire from './NotificationExpire';
import NotificationNewContract from './NotificationNewContract';
import { setReadyForReloadStatistics, setStatistics, setSumUnreadNotification } from '@Redux/Actions/Data';
import { setReadyForReloading as setReadyForReloadNewContract } from '@Redux/Actions/NewContract';
import { setReadyForReloading as setReadyForReloadListContract } from '@Redux/Actions/ListContract';
import { SOCKET_ACTION } from '@Enums/socket';
import ContractService from '@Services/contract';
import AuthService from '@Services/auth';

function NotificationDropdown() {
  const dispatch = useDispatch();
  const user = useSelector(state => state.auth.user);
  const { comment, newContract, expire } = useSelector(state => state.notification);
  const { unreadNotification } = useSelector(state => state.data);
  const unReadRef = useRef(0);
  const { currentPage } = useSelector(state => state.notification.comment);
  const commentRef = useRef(comment);
  const newContractRef = useRef(newContract);
  const expireRef = useRef(expire);
  const [notyLatest, setNotyLatest] = useState(null);

  const getNewestNotify = () => {
    const commentNotify = commentRef.current.notifications[0] || null;
    const newContractNotify = newContractRef.current.notifications[0] || null;
    const expireNotify = expireRef.current.notifications[0] || null;
    const arr = [commentNotify, newContractNotify, expireNotify].filter(n => !!n);
    if (arr.length > 0) {
      const c = arr.sort((a, b) => {
        return new Date(b.createdTime).getTime() - new Date(a.createdTime).getTime();
      });
      setNotyLatest(c[0]);
    } else {
      return null;
    }
  };

  const addNewNotification = (notify, array) => {
    const newArray = [...[notify], ...array];
    // eslint-disable-next-line default-case
    switch (notify.type) {
      case 'NEWCONTRACT':
        if (newArray > newContractRef.current.pageSize) {
          newArray.pop();
        }
        break;
      case 'COMMENT':
        if (newArray > commentRef.current.pageSize) {
          newArray.pop();
        }
        break;
      case 'SIGN':
        if (newArray > commentRef.current.pageSize) {
          newArray.pop();
        }
        break;
      case 'EXPIRE':
        if (newArray > expireRef.current.pageSize) {
          newArray.pop();
        }
        break;
    }
    return newArray;
  };

  const onDropdownToggle = isShow => {
    if (document.querySelector('#notiTabContent .tab-pane.active')) {
      document.querySelector('#notiTabContent .tab-pane.active').classList.remove('active');
      document.querySelector('#notiTabContent .tab-pane').className = document.querySelector('#notiTabContent .tab-pane').className + ' show active';
    }
    if (document.querySelector('#notiTab a.active')) {
      document.querySelector('#notiTab a.active').classList.remove('active');
      document.querySelector('#notiTab a').className = document.querySelector('#notiTab a').className + ' active';
    }
    if (isShow) {
      setNotyLatest(null);
      getNewestNotify();
    } else {
      setNotyLatest(null);
    }
  };

  const getNotiReply = async () => {
    const res = await AuthService.getNotificationType({ size: 15, currentPage: 1 });
    const data = {
      currentPage: currentPage,
      pageSize: 15,
      totalItems: res.total || 15,
      totalPage: res.totalPage || 1,
      notifications: res.data || [],
    };
    dispatch(setCommentNotifications(data));
  };

  useEffect(() => {
    const token = get('digital_signature_token');
    Socket.startConnect(token, () => {
      Socket.connection.emit('notify', {
        type: SOCKET_ACTION.JOIN_DASHBOARD,
      });
      Socket.connection.on('disconnect', () => {
        getNotiReply();
      });
    });
    Socket.onNotify(e => {
      if (e && e.type && e.isDashboard) {
        // eslint-disable-next-line default-case
        if (e.type == 'NEWCONTRACT' || e.type == 'SIGN' || e.type == 'COMMENT' || e.type == 'EXPIRE') {
          dispatch(setSumUnreadNotification(unReadRef.current + 1));
        }
        switch (e.type) {
          case 'NEWCONTRACT':
            if (newContractRef.current.notifications.filter(item => item.id == e.id).length == 0) {
              const arrayNewContract = addNewNotification(e, newContractRef.current.notifications);
              dispatch(
                setNewContractNotifications({
                  currentPage: newContractRef.current.currentPage,
                  totalItems: newContractRef.current.totalItems + 1,
                  totalPage: Math.ceil((newContractRef.current.totalItems + 1) / newContractRef.current.pageSize),
                  pageSize: newContractRef.current.pageSize,
                  notifications: arrayNewContract,
                  totalUnread: newContractRef.current.totalUnread + 1,
                }),
              );
            }
            break;
          case 'SIGN':
            if (user.id != (e.userId ? e.userId : e.signerId)) {
              if (commentRef.current.notifications.filter(item => item.id == e.id).length == 0) {
                const arraySign = addNewNotification(e, commentRef.current.notifications);
                dispatch(
                  setCommentNotifications({
                    currentPage: commentRef.current.currentPage,
                    totalItems: commentRef.current.totalItems + 1,
                    totalPage: Math.ceil((commentRef.current.totalItems + 1) / commentRef.current.pageSize),
                    pageSize: commentRef.current.pageSize,
                    notifications: arraySign,
                    totalUnread: commentRef.current.totalUnread + 1,
                  }),
                );
              }
            }
            break;
          case 'COMMENT':
            if (commentRef.current.notifications.filter(item => item.id == e.id).length == 0) {
              const arrayComment = addNewNotification(e, commentRef.current.notifications);
              dispatch(
                setCommentNotifications({
                  currentPage: commentRef.current.currentPage,
                  totalItems: commentRef.current.totalItems + 1,
                  totalPage: Math.ceil((commentRef.current.totalItems + 1) / commentRef.current.pageSize),
                  pageSize: commentRef.current.pageSize,
                  notifications: arrayComment,
                  totalUnread: commentRef.current.totalUnread + 1,
                }),
              );
            }
            break;
          case 'EXPIRE':
            if (expireRef.current.notifications.filter(item => item.id == e.id).length == 0) {
              const arrayExpire = addNewNotification(e, expireRef.current.notifications);
              dispatch(
                setExpireNotifications({
                  currentPage: expireRef.current.currentPage,
                  totalItems: expireRef.current.totalItems + 1,
                  totalPage: Math.ceil((expireRef.current.totalItems + 1) / expireRef.current.pageSize),
                  pageSize: expireRef.current.pageSize,
                  notifications: arrayExpire,
                  totalUnread: expireRef.current.totalUnread + 1,
                }),
              );
            }
            break;
        }
      }
      dispatch(setReadyForReloadNewContract(true));
      dispatch(setReadyForReloadListContract(true));
      dispatch(setReadyForReloadStatistics(true));
    });
  }, []);

  useEffect(() => {
    return () => {
      Socket.connection.removeListener('notify');
      Socket.connection.removeListener('disconnect');
      Socket.connection.emit('notify', {
        type: SOCKET_ACTION.LEAVE_DASHBOARD,
      });
    };
  }, []);

  useEffect(() => {
    unReadRef.current = unreadNotification ? unreadNotification : 0;
  }, [unreadNotification]);

  useEffect(() => {
    commentRef.current = comment;
    newContractRef.current = newContract;
    expireRef.current = expire;
  }, [comment, newContract, expire]);

  return (
    <>
      <Dropdown className={`noti_icon custom-dropdown`} onToggle={onDropdownToggle}>
        <Dropdown.Toggle as={CustomToggle}>
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="#fff" className="bi bi-bell" viewBox="0 0 16 16">
            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z" />
          </svg>
          {/* <span className="mobi_count">{getTotalUnread() ? getTotalUnread() : 0}</span> */}
          <span className="mobi_count">{unreadNotification ? unreadNotification : 0}</span>
        </Dropdown.Toggle>
        <Dropdown.Menu className="dropdown-menu-right p-0" renderOnMount={true}>
          <div className="notification_body shadow animated-grow-in">
            <ul className="nav nav-fill" id="notiTab" role="tablist">
              <li className="nav-item">
                <a className={`nav-link ${notyLatest ? (notyLatest.type == 'NEWCONTRACT' ? 'active' : '') : 'active'}`} id="newctr-tab" data-toggle="tab" href="#newctr" role="tab" aria-controls="home" aria-selected="true">
                  Hợp đồng mới ({newContract.totalUnread ? newContract.totalUnread : 0})
                </a>
              </li>
              <li className="nav-item">
                <a className={`nav-link ${notyLatest && (notyLatest.type == 'COMMENT' || notyLatest.type == 'SIGN') ? 'active' : ''}`} id="responses-tab" data-toggle="tab" href="#responses" role="tab" aria-controls="profile" aria-selected="false">
                  Phản hồi ({comment.totalUnread ? comment.totalUnread : 0})
                </a>
              </li>
              <li className="nav-item">
                <a className={`nav-link ${notyLatest && notyLatest.type == 'EXPIRE' ? 'active' : ''}`} id="expiry-tab" data-toggle="tab" href="#expiry" role="tab" aria-controls="contact" aria-selected="false">
                  Sắp hết hạn ({expire.totalUnread ? expire.totalUnread : 0})
                </a>
              </li>
            </ul>
            <div className="tab-content" id="notiTabContent">
              <div key="NEWCONTRACT_tab_key" className={`tab-pane fade show ${notyLatest ? (notyLatest.type == 'NEWCONTRACT' ? 'active' : '') : 'active'}`} id="newctr" role="tabpanel" aria-labelledby="newctr-tab">
                <NotificationNewContract />
              </div>
              <div key="COMMENT_tab_key" className={`tab-pane fade show ${notyLatest && (notyLatest.type == 'COMMENT' || notyLatest.type == 'SIGN') ? 'active' : ''}`} id="responses" role="tabpanel" aria-labelledby="responses-tab">
                <NotificationComment />
              </div>
              <div key="EXPIRE_tab_key" className={`tab-pane fade show ${notyLatest && notyLatest.type == 'EXPIRE' ? 'active' : ''}`} id="expiry" role="tabpanel" aria-labelledby="expiry-tab">
                <NotificationExpire />
              </div>
            </div>
          </div>
        </Dropdown.Menu>
      </Dropdown>
    </>
  );
}
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
  <a
    href=""
    ref={ref}
    onClick={e => {
      e.preventDefault();
      onClick(e);
    }}
    id="notification_center"
  >
    {children}
  </a>
));
export default NotificationDropdown;
