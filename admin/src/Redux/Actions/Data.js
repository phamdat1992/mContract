import { DATA_ACTION } from '@Consts/action';

function setLoadingData(payload) {
   return {
      type: DATA_ACTION.SET_LOADING_DATA,
      payload: payload
   }
}

function setStatistics(payload) {
   return {
      type: DATA_ACTION.SET_STATISTICS,
      payload: payload
   }
}

function setTags(payload) {
   return {
      type: DATA_ACTION.SET_TAGS,
      payload: payload
   }
}

function setReloadContract(payload) {
   return {
      type: DATA_ACTION.SET_RELOAD_CONTRACT,
      payload: payload
   }
}

function setReadyForReloadStatistics(payload) {
   return {
      type: DATA_ACTION.SET_READY_FOR_RELOAD_STATISTICS,
      payload: payload
   }
}
function setIsDisconnect(isDisconnect) {
   return {
      type: DATA_ACTION.SET_IS_DISCONNECT,
      payload: isDisconnect
   }
}
function setSumUnreadNotification(payload) {
   return {
      type: DATA_ACTION.SET_UNREAD_NOTIFICATION,
      payload: payload
   }
}
function setActiveTabNotification(payload) {
   return {
      type: DATA_ACTION.SET_ACTIVE_TAB_NOTIFICATION,
      payload: payload
   }
}
export {
   setLoadingData,
   setStatistics,
   setTags,
   setReloadContract,
   setReadyForReloadStatistics,
   setIsDisconnect,
   setSumUnreadNotification,
   setActiveTabNotification
}