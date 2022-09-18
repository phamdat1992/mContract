import { setCommentNotifications, updateCommentNotifications } from '@Redux/Actions/Notification';
import { useEffect, useState, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setReadyForReloadStatistics, setStatistics, setSumUnreadNotification } from '@Redux/Actions/Data';
import AuthService from '@Services/auth';
import NotificationItem from '../NotificationItem';
import { Spinner } from 'react-bootstrap';
import PerfectScrollbar from 'react-perfect-scrollbar';

const NotificationComment = () => {
  const [isRequesting, setIsRequesting] = useState(false);
  const { comment, newContract, expire } = useSelector(state => state.notification);
  const { unreadNotification } = useSelector(state => state.data);
  const unReadRef = useRef(0);
  const { currentPage, pageSize, totalItems, totalPage, notifications } = useSelector(selector => selector.notification.comment);
  const dispatch = useDispatch();

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
      await AuthService.markedAllNotifications({});
      const newArr = [...[], ...notifications.map(n => {
        return { ...n, status: true };
      })]
      dispatch(setSumUnreadNotification((unReadRef.current ? unReadRef.current : 0) - (comment.totalUnread ? comment.totalUnread : 0)));
      dispatch(setCommentNotifications({ totalUnread: 0, notifications: newArr }));
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
      const res = await AuthService.getNotificationType({ size: pageSize, currentPage: page });
      const data = {
        currentPage: page,
        pageSize: pageSize,
        totalItems: res.total || pageSize,
        totalPage: res.totalPage || 1,
        notifications: res.data || [],
        totalUnread: res.totalUnread || 0
      };
      if (page <= 1) {
        dispatch(setCommentNotifications(data));
      } else {
        dispatch(updateCommentNotifications(data));
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
        <a href="javascript:void(0)" className="markread" onClick={() => markedAll('COMMENT')}>
          Đánh dấu tất cả là đã đọc
        </a>
      )}
    </>
  );
};

export default NotificationComment;
