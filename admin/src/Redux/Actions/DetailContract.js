import { DETAIL_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setContract(contract) {
  return {
    type: ACTION.SET_CONTRACT,
    payload: contract,
  };
}

function updateContract(contract) {
  return {
    type: ACTION.UPDATE_CONTRACT,
    payload: contract,
  };
}

function setComments(comments) {
  return {
    type: ACTION.SET_COMMENTS,
    payload: comments,
  };
}

function setNewComment(comment) {
  return {
    type: ACTION.SET_NEW_COMMENT,
    payload: comment,
  };
}

function setStartComment(value) {
  return {
    type: ACTION.SET_START_COMMENT,
    payload: value,
  };
}

function createNewComment(comment) {
  return {
    type: ACTION.CREATE_NEW_COMMENT,
    payload: comment,
  };
}

function addNewComment(comment) {
  return {
    type: ACTION.ADD_NEW_COMMENT,
    payload: comment,
  };
}

function setFocusComment(comment) {
  return {
    type: ACTION.SET_FOCUS_COMMENT,
    payload: comment,
  };
}
function setFocusSign(sign) {
  return {
    type: ACTION.SET_FOCUS_SIGN,
    payload: sign,
  };
}

function addNewNotification(notification) {
  return {
    type: ACTION.ADD_NEW_NOTIFICATION,
    payload: notification,
  };
}

function removeNotification(notifyId) {
  return {
    type: ACTION.REMOVE_NOTIFICATION,
    payload: notifyId,
  };
}

function setCurrentSigner(signer) {
  return {
    type: ACTION.SET_CURRENT_SIGNER,
    payload: signer,
  };
}

function addComment(comment) {
  return {
    type: ACTION.ADD_COMMENT,
    payload: comment,
  };
}

function addChildComment(comment) {
  return {
    type: ACTION.ADD_CHILD_COMMENT,
    payload: comment,
  };
}

function setCurrentSignerToken(token) {
  return {
    type: ACTION.SET_CURRENT_SIGNER_TOKEN,
    payload: token,
  };
}

function setPreviewCurrentPage(page) {
  return {
    type: ACTION.SET_PREVIEW_CURRENT_PAGE,
    payload: page,
  };
}

function setPreviewTotalPages(totalPages) {
  return {
    type: ACTION.SET_PREVIEW_TOTAL_PAGES,
    payload: totalPages,
  };
}

function setPdfCurrentPage(page) {
  return {
    type: ACTION.SET_PDF_CURRENT_PAGE,
    payload: page,
  };
}

function setPdfTotalPages(totalPages) {
  return {
    type: ACTION.SET_PDF_TOTAL_PAGES,
    payload: totalPages,
  };
}

function resetData() {
  return {
    type: ACTION.RESET_DATA,
  };
}

function setContractTags(tags) {
  return {
    type: ACTION.SET_CONTRACT_TAGS,
    payload: tags,
  };
}
function setNotificationsUnread(notificationsUnread) {
  return {
    type: ACTION.SET_NOTIFICATIONS_UNREAD,
    payload: notificationsUnread,
  };
}

function updateComments(data) {
  return {
    type: ACTION.UPDATE_COMMENTS,
    payload: data,
  };
}

function updateNotifications(data) {
  return {
    type: ACTION.UPDATE_NOTIFICATIONS,
    payload: data,
  };
}

function setShowSignerModal(isShowSignerModal) {
  return {
    type: ACTION.SET_SHOW_SIGNER_MODAL,
    payload: isShowSignerModal,
  };
}

function setCanceling(isCanceling) {
  return {
    type: ACTION.SET_CANCELING,
    payload: isCanceling,
  }
}

function setLoadedAllPageDetail(isLoadedAllPageDetail) {
  return {
    type: ACTION.SET_LOADED_ALL_PAGE_DETAIL,
    payload: isLoadedAllPageDetail,
  }
}

function setLoadedAllPagePreviewDetail(isLoadedAllPagePreviewDetail) {
  return {
    type: ACTION.SET_LOADED_ALL_PAGE_PREVIEW_DETAIL,
    payload: isLoadedAllPagePreviewDetail,
  }
}

function setIsDisconnect(isDisconnect) {
  return {
    type: ACTION.SET_IS_DISCONNECT,
    payload: isDisconnect
  }
}

function setFocusRealtimeComment(comment) {
  return {
    type: ACTION.SET_FOCUS_REALTIME_COMMENT,
    payload: comment
  }
}

export {
  setContract,
  updateContract,
  setComments,
  setNewComment,
  addNewComment,
  setStartComment,
  createNewComment,
  setFocusComment,
  addNewNotification,
  removeNotification,
  setCurrentSigner,
  setCurrentSignerToken,
  addComment,
  addChildComment,
  setPreviewCurrentPage,
  setPreviewTotalPages,
  setPdfCurrentPage,
  setPdfTotalPages,
  resetData,
  setContractTags,
  setNotificationsUnread,
  updateComments,
  updateNotifications,
  setShowSignerModal,
  setFocusSign,
  setCanceling,
  setLoadedAllPageDetail,
  setLoadedAllPagePreviewDetail,
  setIsDisconnect,
  setFocusRealtimeComment
};
