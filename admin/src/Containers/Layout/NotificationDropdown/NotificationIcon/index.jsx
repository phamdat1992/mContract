import { IconChatRightText, IconClockHistory, IconFileText, IconPencil } from '@Components/Icon';

function NotificationIcon({ category }) {
  switch (category) {
    case 'NEWCONTRACT':
      return (
        <>
          <IconFileText />
        </>
      );
    case 'SIGN':
      return (
        <>
          <IconPencil />
        </>
      );
    case 'COMMENT':
      return (
        <>
          <IconChatRightText />
        </>
      );
    case 'EXPIRE':
      return (
        <>
          <IconClockHistory />
        </>
      );
    default:
      return (
        <>
          <IconFileText />
        </>
      );
  }
}

export default NotificationIcon;