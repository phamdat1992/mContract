import { DATA_ACTION } from "@Consts/action";

function setLoadingData(state, action) {
   return {
      ...state,
      isLoadingData: action.payload,
   };
}
function setStatistics(state, action) {
   return {
      ...state,
      statistics: action.payload,
   };
}

function setReadyForReloadStatistics(state, action) {
   return {
      ...state,
      readyForReloadStatistics: action.payload,
   };
}


function setTags(state, action) {
   return {
      ...state,
      tags: action.payload,
   };
}
function setReloadContract(state, action) {
   return {
      ...state,
      needReload: action.payload,
   };
}
function setIsDisconnect(state, isDisconnect) {
   return {
      ...state,
      ...{ isDisconnect: isDisconnect },
   };
}
function setSumUnreadNotification(state, action) {
   return {
      ...state,
      unreadNotification: action.payload,
   };
}
function setActiveTabNotification(state, action) {
   return {
      ...state,
      activeTabNotification: action.payload,
   };
}

const initialState = {
   isLoadingData: false,
   isDisconnect: false,
   statistics: null,
   tags: null,
   needReload: false,
   readyForReloadStatistics: false,
   activeTabNotification: null,
   unreadNotification: null
};
function DataReducer(state = initialState, action) {
   switch (action.type) {
      case DATA_ACTION.SET_LOADING_DATA:
         return setLoadingData(state, action);
      case DATA_ACTION.SET_STATISTICS:
         return setStatistics(state, action);
      case DATA_ACTION.SET_TAGS:
         return setTags(state, action)
      case DATA_ACTION.SET_RELOAD_CONTRACT:
         return setReloadContract(state, action);
      case DATA_ACTION.SET_READY_FOR_RELOAD_STATISTICS:
         return setReadyForReloadStatistics(state, action);
      case DATA_ACTION.SET_IS_DISCONNECT:
         return setIsDisconnect(state, action);
      case DATA_ACTION.SET_UNREAD_NOTIFICATION:
         return setSumUnreadNotification(state, action);
      case DATA_ACTION.SET_ACTIVE_TAB_NOTIFICATION:
         return setActiveTabNotification(state, action);
      default:
         return state;
   }
}
export default DataReducer;
