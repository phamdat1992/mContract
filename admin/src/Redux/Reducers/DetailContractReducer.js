import { DETAIL_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setContract(state, contract) {
  return {
    ...state,
    ...{ contract: contract },
  };
}

function updateContract(state, data) {
  return {
    ...state,
    ...{ contract: { ...state.contract, ...data } },
  };
}

function setComments(state, comments) {
  return {
    ...state,
    ...{ comments: comments },
  };
}

function setNewComment(state, comment) {
  return {
    ...state,
    ...{ newComment: comment },
  };
}

function addNewComment(state, comment) {
  return {
    ...state,
    ...{ comments: [...state.comments, comment] },
  };
}

function setStartComment(state, value) {
  return {
    ...state,
    ...{ startComment: value },
  };
}
function sortNotification(arr) {
  return arr.sort((a, b) => parseInt(new Date(b.createdTime).getTime()) - parseInt(new Date(a.createdTime).getTime()));
}
function addNewNotification(state, notification) {
  return {
    ...state,
    ...{ notifications: sortNotification([...state.notifications, ...[notification]]) },
  };
}

function removeNotification(state, notificationId) {
  return {
    ...state,
    ...{ notifications: state.notifications.filter(n => n.id != notificationId) },
  };
}

function setCurrentSigner(state, signer) {
  return {
    ...state,
    ...{ currentSigner: signer },
  };
}

function setFocusComment(state, comment) {
  return {
    ...state,
    ...{ focusComment: comment },
  };
}

function setFocusSign(state, sign) {
  return {
    ...state,
    ...{ focusSign: sign },
  };
}

function setCurrentSignerToken(state, token) {
  return {
    ...state,
    ...{ currentSignerToken: token },
  };
}

function addComment(state, comment) {
  return {
    ...state,
    ...{ comments: [...state.comments, ...[comment]] },
  };
}

function appendChildComment(comments, childComment) {
  return comments.map(comment => {
    if (comment.id == childComment.parentId) {
      return {
        ...comment,
        ...{ childCommentDtos: comment.childCommentDtos ? [...comment.childCommentDtos, ...[childComment]] : [childComment] },
      };
    } else {
      if (comment.childCommentDtos && comment.childCommentDtos.length > 0) {
        return {
          ...comment,
          ...{ childCommentDtos: appendChildComment(comment.childCommentDtos, childComment) },
        };
      } else {
        return comment;
      }
    }
  });
}

function addChildComment(state, childComment) {
  return { ...state, ...{ comments: appendChildComment(state.comments, childComment) } };
}

function setPreviewCurrentPage(state, currentPage) {
  return {
    ...state,
    ...{ preview: { ...state.preview, ...{ currentPage: currentPage } } },
  };
}

function setPreviewTotalPages(state, totalPages) {
  return {
    ...state,
    ...{ preview: { ...state.preview, ...{ totalPages: totalPages } } },
  };
}

function setPdfCurrentPage(state, currentPage) {
  return {
    ...state,
    ...{ pdf: { ...state.pdf, ...{ currentPage: currentPage } } },
  };
}

function setPdfTotalPages(state, totalPages) {
  return {
    ...state,
    ...{ pdf: { ...state.pdf, ...{ totalPages: totalPages } } },
  };
}

function setContractTags(state, tags) {
  const contract = { ...state.contract, ...{ tagDtos: tags } };
  return {
    ...state,
    ...{ contract: contract }
  }
}
function setNotifications(state, notifications) {
  return {
    ...state,
    ...{ notifications: notifications },
  };
}

function resetData() {
  return initialState;
}

function updateComments(state, data) {
  const newComments = Object.assign([], state.comments);
  newComments.forEach((item, index) => {
    if (data.id == item.id) {
      newComments[index] = {
        ...item,
        ...{ isRead: true }
      }
    } else {
      if (item.childCommentDtos) {
        const newChildComments = item.childCommentDtos.map((child, pos) => {
          if (data.id == child.id) {
            return {
              ...child,
              ...{ isRead: true }
            }
          }
          return child;
        });
        newComments[index] = { ...item, ...{ childCommentDtos: newChildComments } };
      }
    }
  });
  return {
    ...state,
    ...{ comments: newComments },
  };
}

function updateNotifications(state, data) {
  const newNotifications = Object.assign([], state.notifications);
  newNotifications.forEach((item, index) => {
    if (data.id == item.id) {
      newNotifications[index] = {
        ...item,
        ...{ status: true }
      }
    }
  });

  return {
    ...state,
    ...{ notifications: newNotifications },
  };
}

function setShowSignerModal(state, isShowSignerModal) {
  return {
    ...state,
    isShowSignerModal: isShowSignerModal
  }
}
function setCanceling(state, isCanceling) {
  return {
    ...state,
    ...{ isCanceling: isCanceling },
  };
}

function setLoadedAllPageDetail(state, isLoadedAllPageDetail) {
  return {
    ...state,
    ...{ isLoadedAllPageDetail: isLoadedAllPageDetail },
  };
}

function setLoadedAllPagePreviewDetail(state, isLoadedAllPagePreviewDetail) {
  return {
    ...state,
    ...{ isLoadedAllPagePreviewDetail: isLoadedAllPagePreviewDetail },
  };
}

function setIsDisconnect(state, isDisconnect) {
  return {
    ...state,
    ...{ isDisconnect: isDisconnect },
  };
}
function setFocusRealtimeComment(state, focusRealtimeComment) {
  return {
    ...state,
    ...{ focusRealtimeComment: focusRealtimeComment },
  };
}

const initialState = {
  contract: null,
  comments: [],
  notifications: [],
  newComment: null,
  focusComment: null,
  startComment: false,
  isCanceling: false,
  currentSigner: null,
  currentSignerToken: null,
  notificationsUnread: null,
  isShowSignerModal: false,
  isLoadedAllPageDetail: false,
  isLoadedAllPagePreviewDetail: false,
  setIsDisconnect: false,
  focusSign: null,
  focusRealtimeComment: null,
  preview: {
    currentPage: 1,
    totalPages: 1,
  },
  pdf: {
    currentPage: 1,
    totalPages: 1,
  },
  tags: []
};

function ContractReducer(state = initialState, action) {
  switch (action.type) {
    case ACTION.SET_CONTRACT:
      return setContract(state, action.payload);
    case ACTION.UPDATE_CONTRACT:
      return updateContract(state, action.payload);
    case ACTION.SET_COMMENTS:
      return setComments(state, action.payload);
    case ACTION.SET_NEW_COMMENT:
      return setNewComment(state, action.payload);
    case ACTION.SET_START_COMMENT:
      return setStartComment(state, action.payload);
    case ACTION.SET_FOCUS_COMMENT:
      return setFocusComment(state, action.payload);
    case ACTION.ADD_NEW_COMMENT:
      return addNewComment(state, action.payload);
    case ACTION.ADD_NEW_NOTIFICATION:
      return addNewNotification(state, action.payload);
    case ACTION.REMOVE_NOTIFICATION:
      return removeNotification(state, action.payload);
    case ACTION.SET_CURRENT_SIGNER:
      return setCurrentSigner(state, action.payload);
    case ACTION.SET_CURRENT_SIGNER_TOKEN:
      return setCurrentSignerToken(state, action.payload);
    case ACTION.ADD_COMMENT:
      return addComment(state, action.payload);
    case ACTION.ADD_CHILD_COMMENT:
      return addChildComment(state, action.payload);
    case ACTION.SET_PREVIEW_CURRENT_PAGE:
      return setPreviewCurrentPage(state, action.payload);
    case ACTION.SET_PREVIEW_TOTAL_PAGES:
      return setPreviewTotalPages(state, action.payload);
    case ACTION.SET_PDF_CURRENT_PAGE:
      return setPdfCurrentPage(state, action.payload);
    case ACTION.SET_PDF_TOTAL_PAGES:
      return setPdfTotalPages(state, action.payload);
    case ACTION.SET_CONTRACT_TAGS:
      return setContractTags(state, action.payload);
    case ACTION.SET_NOTIFICATIONS_UNREAD:
      return setNotifications(state, action.payload);
    case ACTION.RESET_DATA:
      return resetData(state);
    case ACTION.UPDATE_NOTIFICATIONS:
      return updateNotifications(state, action.payload);
    case ACTION.UPDATE_COMMENTS:
      return updateComments(state, action.payload);
    case ACTION.SET_SHOW_SIGNER_MODAL:
      return setShowSignerModal(state, action.payload)
    case ACTION.SET_FOCUS_SIGN:
      return setFocusSign(state, action.payload);
    case ACTION.SET_CANCELING:
      return setCanceling(state, action.payload);
    case ACTION.SET_LOADED_ALL_PAGE_DETAIL:
      return setLoadedAllPageDetail(state, action.payload);
    case ACTION.SET_LOADED_ALL_PAGE_PREVIEW_DETAIL:
      return setLoadedAllPagePreviewDetail(state, action.payload);
    case ACTION.SET_IS_DISCONNECT:
      return setIsDisconnect(state, action.payload);
    case ACTION.SET_FOCUS_REALTIME_COMMENT:
      return setFocusRealtimeComment(state, action.payload);
    default:
      return state;
  }
}
export default ContractReducer;
