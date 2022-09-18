import { NOTIFICATION_ACTION } from '@Consts/action';

function setNotifications(notifications) {
  return {
    type: NOTIFICATION_ACTION.SET_NOTIFICATIONS,
    payload: notifications,
  };
}

function addNotification(notification) {
  return {
    type: NOTIFICATION_ACTION.ADD_NOTIFICATION,
    payload: notification,
  };
}

function removeNotification(id) {
  return {
    type: NOTIFICATION_ACTION.REMOVE_NOTIFICATION,
    payload: id,
  };
}

function updateAllNotifications(notifications) {
  return {
    type: NOTIFICATION_ACTION.UPDATE_ALL_NOTIFICATIONS,
    payload: notifications,
  };
}

function setNewContractNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.SET_NEW_CONTRACT_NOTIFICATIONS,
    payload: data,
  };
}

function updateNewContractNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.UPDATE_NEW_CONTRACT_NOTIFICATIONS,
    payload: data,
  };
}

function setCommentNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.SET_COMMENT_NOTIFICATIONS,
    payload: data,
  };
}

function updateCommentNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.UPDATE_COMMENT_NOTIFICATIONS,
    payload: data,
  };
}

function setExpireNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.SET_EXPIRE_NOTIFICATIONS,
    payload: data,
  };
}

function updateExpireNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.UPDATE_EXPIRE_NOTIFICATIONS,
    payload: data,
  };
}
function upDateStatusNotifications(data) {
  return {
    type: NOTIFICATION_ACTION.UPDATE_STATUS_NOTIFICATIONS,
    payload: data,
  };
}

export { setNotifications, addNotification, removeNotification, updateAllNotifications, setNewContractNotifications, updateNewContractNotifications, setCommentNotifications, updateCommentNotifications, setExpireNotifications, updateExpireNotifications, upDateStatusNotifications };
