import { setNewContractNotifications, updateNewContractNotifications, upDateStatusNotifications } from '@Redux/Actions/Notification';
import { useEffect, useState, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import AuthService from '@Services/auth';
import NotificationItem from '../NotificationItem';
import { Spinner } from 'react-bootstrap';
import PerfectScrollbar from 'react-perfect-scrollbar';
import { setReadyForReloadStatistics, setStatistics, setSumUnreadNotification } from '@Redux/Actions/Data';

const NotificationNewContract = () => {
  const [isRequesting, setIsRequesting] = useState(false);
  const { comment, newContract, expire } = useSelector(state => state.notification);
  const { unreadNotification } = useSelector(state => state.data);
  const { currentPage, pageSize, totalItems, totalPage, notifications } = useSelector(selector => selector.notification.newContract);
  const dispatch = useDispatch();
  const unReadRef = useRef(0);

  const onScroll = e => {
    if (!isRequesting) {
      const contain = e.target;
      if (contain.scrollHeight - contain.offsetHeight - contain.scrollTop < 50 && (currentPage < totalPage)) {
        getNotifications(currentPage + 1);
      }
    }
  };

  const markedAll = async () => {
    try {
      await AuthService.markedAllNotifications({ type: 'NEWCONTRACT' });
      const newArr = [...[], ...notifications.map(n => {
        return { ...n, status: true };
      })]
      dispatch(setSumUnreadNotification((unReadRef.current ? unReadRef.current : 0) - (newContract.totalUnread ? newContract.totalUnread : 0)));
      dispatch(setNewContractNotifications({ totalUnread: 0, notifications: newArr }))
      // dispatch(upDateStatusNotifications(notifications));
    } catch (err) {
      console.error(err);
    }
  };

  /**
   * Lấy danh sách Notification theo Page
   * Default Page = 1
   * Nếu page = 1 thì reset notifications trong redux
   */
  const getNotifications = async (page = 1) => {
    try {
      setIsRequesting(true);
      const res = await AuthService.getNotificationType({ type: 'NEWCONTRACT', size: pageSize, currentPage: page });
      const data = {
        currentPage: page,
        pageSize: pageSize,
        totalItems: res.total || pageSize,
        totalPage: res.totalPage || 1,
        notifications: res.data || [],
        totalUnread: res.totalUnread || 0
      };
      if (page <= 1) {
        dispatch(setNewContractNotifications(data));
      } else {
        dispatch(updateNewContractNotifications(data));
      }
    } catch (err) {
      console.error(err);
    } finally {
      setIsRequesting(false);
    }
  };
  useEffect(() => {
    unReadRef.current = unreadNotification ? unreadNotification : 0;
  }, [unreadNotification]);

  useEffect(() => {
    // if (notifications.length == 0) {
    getNotifications();
    // }
  }, []);

  return (
    <>
      {currentPage > 0 && notifications.length > 0 ? (
        <>
          <PerfectScrollbar suppressScrollX="true" onScroll={e => onScroll(e)}>
            <div className="scrollnoti_wrapper">
              {notifications.map((noti, index) => {
                return <NotificationItem notification={noti} key={`notification_${noti.id}`} />;
              })}
              {isRequesting && (
                <div className="p-2">
                  <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
                </div>
              )}
            </div>
          </PerfectScrollbar>
        </>
      ) : (
        <>
          <div className="p-2">Không có thông báo nào.</div>
        </>
      )}

      {currentPage > 0 && notifications.length > 0 && (
        <a href="javascript:void(0)" className="markread" onClick={() => markedAll()}>
          Đánh dấu tất cả là đã đọc
        </a>
      )}
    </>
  );
};

export default NotificationNewContract;
