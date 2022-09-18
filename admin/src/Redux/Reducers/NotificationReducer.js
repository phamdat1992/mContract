import { NOTIFICATION_ACTION } from '@Consts/action';

function setNotifications(state, notifications) {
  return {
    ...state,
    notifications: notifications,
  };
}
function addNotification(state, notification) {
  return {
    ...state,
    ...{ notifications: [...[notification], ...state.notifications] },
  };
}
function updateAllNotifications(state, type) {
  const newNotifications = Object.assign([], state.notifications);
  if (type == 'COMMENT') {
    newNotifications.forEach((item, index) => {
      if (item.type == 'COMMENT' || item.type == 'SIGN') {
        newNotifications[index] = {
          ...item,
          ...{ status: true },
        };
      }
      return newNotifications[index];
    });
  } else {
    newNotifications.forEach((item, index) => {
      if (item.type == type) {
        newNotifications[index] = {
          ...item,
          ...{ status: true },
        };
      }
      return newNotifications[index];
    });
  }

  return {
    ...state,
    ...{ notifications: newNotifications },
  };
}

function setNewContractNotifications(state, data) {
  return {
    ...state,
    ...{ newContract: { ...state.newContract, ...data } },
  };
}

function updateNewContractNotifications(state, data) {
  return {
    ...state,
    ...{
      newContract: {
        currentPage: data.currentPage,
        pageSize: data.pageSize || state.newContract.pageSize,
        totalItems: data.totalItems || state.newContract.totalItems,
        totalPage: data.totalPage || state.newContract.totalPage,
        notifications: data.notifications ? [...state.newContract.notifications, ...data.notifications] : [...state.newContract.notifications],
        totalUnread: data.totalUnread || state.newContract.totalUnread,
      },
    },
  };
}

function setCommentNotifications(state, data) {
  return {
    ...state,
    ...{ comment: { ...state.comment, ...data } },
  };
}

function updateCommentNotifications(state, data) {
  return {
    ...state,
    ...{
      comment: {
        currentPage: data.currentPage,
        pageSize: data.pageSize || state.comment.pageSize,
        totalItems: data.totalItems || state.comment.totalItems,
        totalPage: data.totalPage || state.comment.totalPage,
        notifications: data.notifications ? [...state.comment.notifications, ...data.notifications] : [...state.comment.notifications],
        totalUnread: data.totalUnread || state.comment.totalUnread,
      },
    },
  };
}

function setExpireNotifications(state, data) {
  return {
    ...state,
    ...{ expire: { ...state.expire, ...data } },
  };
}

function updateExpireNotifications(state, data) {
  return {
    ...state,
    ...{
      expire: {
        currentPage: data.currentPage,
        pageSize: data.pageSize || state.expire.pageSize,
        totalItems: data.totalItems || state.expire.totalItems,
        totalPage: data.totalPage || state.expire.totalPage,
        notifications: data.notifications ? [...state.expire.notifications, ...data.notifications] : [...state.expire.notifications],
        totalUnread: data.totalUnread || state.expire.totalUnread,
      },
    },
  };
}

function upDateStatusNotifications(state, data) {
  const notifications = Object.assign([], data);
  notifications.forEach((item, index) => {
    notifications[index] = {
      ...item,
      ...{ status: true }
    }
  });
  return {
    ...state,
    ...{ notifications: notifications },
  };

}

const initialState = {
  notifications: [],
  newContract: {
    currentPage: 0,
    totalItems: 0,
    totalPage: 0,
    pageSize: 15,
    notifications: [],
    totalUnread: 0
  },
  comment: {
    currentPage: 0,
    totalItems: 0,
    totalPage: 0,
    pageSize: 15,
    notifications: [],
    totalUnread: 0
  },
  expire: {
    currentPage: 0,
    totalItems: 0,
    totalPage: 0,
    pageSize: 15,
    notifications: [],
    totalUnread: 0
  },
};

function NotificationReducer(state = initialState, action) {
  switch (action.type) {
    case NOTIFICATION_ACTION.SET_NOTIFICATIONS:
      return setNotifications(state, action.payload);
    case NOTIFICATION_ACTION.ADD_NOTIFICATION:
      return addNotification(state, action.payload);
    case NOTIFICATION_ACTION.UPDATE_ALL_NOTIFICATIONS:
      return updateAllNotifications(state, action.payload);
    case NOTIFICATION_ACTION.SET_NEW_CONTRACT_NOTIFICATIONS:
      return setNewContractNotifications(state, action.payload);
    case NOTIFICATION_ACTION.UPDATE_NEW_CONTRACT_NOTIFICATIONS:
      return updateNewContractNotifications(state, action.payload);
    case NOTIFICATION_ACTION.SET_COMMENT_NOTIFICATIONS:
      return setCommentNotifications(state, action.payload);
    case NOTIFICATION_ACTION.UPDATE_COMMENT_NOTIFICATIONS:
      return updateCommentNotifications(state, action.payload);
    case NOTIFICATION_ACTION.SET_EXPIRE_NOTIFICATIONS:
      return setExpireNotifications(state, action.payload);
    case NOTIFICATION_ACTION.UPDATE_EXPIRE_NOTIFICATIONS:
      return updateExpireNotifications(state, action.payload);
    case NOTIFICATION_ACTION.UPDATE_STATUS_NOTIFICATIONS:
      return upDateStatusNotifications(state, action.payload);
    default:
      return state;
  }
}

export default NotificationReducer;
