import NotificationTime from "../NotificationTime";

function NotificationStatus({ notification }) {
  switch (notification.type) {
    case 'NEWCONTRACT':
      return (
        <>
          <span>Đã gửi:</span> <span className="time"> <NotificationTime key={`notification_time_${notification.id}`} notify={notification} /></span>
        </>
      );
    case 'SIGN':
      return (
        <>
          <span>Đã ký:</span> <span className="time"><NotificationTime key={`notification_time_${notification.id}`} notify={notification} /></span>
        </>
      );
    case 'COMMENT':
      return (
        <>
          <span>Đã phản hồi:</span> <span className="time"><NotificationTime key={`notification_time_${notification.id}`} notify={notification} /></span>
        </>
      );
    case 'EXPIRE':
      return (
        <>
          <span>Hết hạn vào:</span> <span className="time"><NotificationTime key={`notification_time_${notification.id}`} notify={notification} /></span>
        </>
      );

    default:
      return <></>;
  }
}
export default NotificationStatus;