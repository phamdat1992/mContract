import NotificationStatus from '../NotificationStatus';
import { setCommentNotifications, setNewContractNotifications, setExpireNotifications } from '@Redux/Actions/Notification';
import NotificationIcon from '../NotificationIcon';
import { useSelector, useDispatch } from 'react-redux';
import { Link, useHistory, useLocation } from 'react-router-dom';
import {} from 'react-router-dom';
import { setFocusComment, setFocusSign } from '@Redux/Actions/DetailContract';
import { setPdfCurrentPage, setPreviewCurrentPage } from '@Redux/Actions/DetailContract';
import CommentService from '@Services/comment';
import GeneralService from '@Services/general';
import AuthService from '@Services/auth';
import { setReadyForReloadStatistics, setStatistics, setSumUnreadNotification } from '@Redux/Actions/Data';
function NotificationItem({ notification }) {
  const { notifications } = useSelector(selector => selector.notification.comment);
  const { unreadNotification } = useSelector(state => state.data);
  const dispatch = useDispatch();
  const history = useHistory();

  function getContent(notification) {
    switch (notification.type) {
      case 'NEWCONTRACT':
        return `${notification.contractContent}`;
      case 'SIGN':
        return `${notification.contractContent}`;
      case 'COMMENT':
        return `${notification.commentContent}`;
      case 'EXPIRE':
        return `${notification.contractContent}`;
      default:
        return `${notification.contractContent}`;
    }
  }

  function onFocusComment(notify) {
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
      createdTime: new Date(notify.commentCreatedTime ? notify.commentCreatedTime : notify.createdTime).toISOString(),
      isRead: notify.status ? notify.status : false,
    };
    if (notify.type == 'COMMENT' || notify.type == 'SIGN') {
      if (notify.type == 'COMMENT') {
        dispatch(setFocusComment(notifyData));
      }
      if (notify.type == 'SIGN') {
        dispatch(setFocusSign(notifyData));
      }
      dispatch(setPdfCurrentPage(notifyData.page));
      dispatch(setPreviewCurrentPage(notifyData.page));
    }
  }

  async function markedSingle(notify) {
    // if (notify.type == 'COMMENT' || notify.type == 'SIGN') {
    //   onFocusComment(notify);
    // }
    if (!notify.status) {
      await AuthService.markedNotification(notify.contractId, notify.id);
      dispatch(setSumUnreadNotification(unreadNotification ? unreadNotification - 1 : 0));
      const newArr = [
        ...[],
        ...notifications.map(n => {
          if (n.id == notify.id) {
            return { ...n, status: true };
          }
          return n;
        }),
      ];
      switch (notify.type) {
        case 'NEWCONTRACT':
          dispatch(setNewContractNotifications({ notifications: newArr }));
          break;
        case 'COMMENT':
          dispatch(setCommentNotifications({ notifications: newArr }));
          break;
        case 'SIGN':
          dispatch(setCommentNotifications({ notifications: newArr }));
          break;
        case 'EXPIRE':
          dispatch(setExpireNotifications({ notifications: newArr }));
          break;
      }
      if (notify.type == 'COMMENT') {
        await CommentService.markCommentUser(notify.commentId ? notify.commentId : notify.data.id);
      }
    }
    switch (notify.type) {
      case 'COMMENT':
        history.push(`/hop-dong/${notify.contractId}?commentId=${notify.commentId}`);
        break;
      case 'SIGN':
        history.push(`/hop-dong/${notify.contractId}?signerId=${notify.signerId ? notify.signerId : notify.userId}`);
        break;
      default:
        history.push(`/hop-dong/${notify.contractId}`);
    }
  }

  return (
    <a style={{ cursor: 'pointer' }} className={`noti_item ${notification.status ? '' : 'unread'}`} onClick={() => markedSingle(notification)}>
      {/* <a style={{ cursor: 'pointer' }} to={`/hop-dong/${notification.contractId}`} className={`noti_item ${notification.status ? "" : "unread"}`} onClick={() => markedSingle(notification.id)}> */}
      <div className="row mx-0 px-1">
        <div className="col-auto pr-0 pl-2">
          {notification.userAvatar ? (
            <img
              src={notification.userAvatar ? notification.userAvatar : ''}
              className="rounded-circle person_img"
              onError={e => {
                e.target.onerror = null;
                // e.target.parentNode.innerHTML = (`<div class="pimg_alt">${GeneralService.getLetterName(notification.userName ? notification.userName : notification.signerFullName)}</div> <div className="noti_sicon">${<NotificationIcon key={`notification_icon_${notification.id}`} notification={notification.type} />}</div>`);
              }}
              alt=""
            />
          ) : (
            <div className="pimg_alt">{GeneralService.getLetterName(notification.userName ? notification.userName : notification.signerFullName)}</div>
          )}
          <div className="noti_sicon">
            <NotificationIcon key={`notification_icon_${notification.id}`} notification={notification.type} />
          </div>
        </div>
        <div className="col noti_text">
          <div>
            <div className="noti_name">
              {notification.signerFullName ? notification.signerFullName : notification.userName} - {notification.contractName}
            </div>
            <div className="noti_content">{getContent(notification)}</div>
          </div>
          <div className="noti_meta">
            <NotificationStatus key={`notification_status_${notification.id}`} notification={notification} />
          </div>
        </div>
      </div>
      {/* </a> */}
    </a>
  );
}

export default NotificationItem;
