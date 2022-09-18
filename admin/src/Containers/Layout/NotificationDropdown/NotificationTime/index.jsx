import { timeDesc } from '@Utils/helpers';
import { useEffect, useState } from 'react';

const NotificationTime = ({ notify }) => {
  const [time, setTime] = useState(timeDesc(new Date(notify.createdTime)));
  let timeout;
  useEffect(() => {
    const createdDate = new Date(notify.createdTime);
    const today = new Date();
    if (notify.type.toUpperCase() != 'EXPIRE') {
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
    } else {
      const d = new Date(notify.expiredTime);
      setTime(('0' + d.getDate()).slice(-2) + '/' + ('0' + (d.getMonth() + 1)).slice(-2) + '/' + d.getFullYear());
    }
  }, []);
  return <>{time}</>;
};

export default NotificationTime;