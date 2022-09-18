import React, { useEffect, useRef, useState } from 'react';
import { IconChatYellow } from '@Components/Icon';
import { Spinner } from 'react-bootstrap';
import CommentService from '@Services/comment';
import { useDispatch, useSelector } from 'react-redux';
import { addComment, addChildComment, setComments, setPreviewCurrentPage, setPdfCurrentPage, setFocusComment, setFocusSign, setNewComment, updateNotifications, updateComments, setFocusRealtimeComment } from '@Redux/Actions/DetailContract';
import GeneralService from '@Services/general';
import ContractService from '@Services/contract';
import { useLocation } from 'react-router-dom';
import { addToast } from '@Redux/Actions/AlertToast';

const commentPositionPercent = (pageNumber, x, y) => {
  const page = document.querySelector(`#showPDFSign .pdf-page[data-page-number="${pageNumber}"]`);
  return {
    percentX: (x / page.offsetWidth) * 100,
    percentY: (y / page.offsetHeight) * 100,
  };
};

const getDate = ms => {
  const date = new Date(ms);
  return `${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)} - ${('0' + date.getDate()).slice(-2)}/${('0' + (date.getMonth() + 1)).slice(-2)}/${date.getFullYear()}`;
};

const Comment = ({ comment, index, child = false, contract, currentSignerToken, setTabKey }) => {
  const [counter, setCounter] = useState(512);
  const {
    notifications,
    comments,
    currentSigner,
    pdf: { currentPage },
  } = useSelector(state => state.detailContract);
  const user = useSelector(state => state.auth.user);

  const dispatch = useDispatch();

  const [isReply, setIsReply] = useState(false);
  const [isReling, setIsReling] = useState('');
  const [contentComment, setContentComment] = useState('');
  const location = useLocation();
  let textareaRef = useRef(null);

  const submitReplyComment = async event => {
    event.preventDefault();
    if (textareaRef.current.innerText.length < 513) {
      try {
        setIsReling(true);
        const data = {
          contractId: contract.id,
          content: textareaRef.current.innerText.substring(0, 511),
          parentId: comment.parentId ? comment.parentId : comment.id,
          page: null,
          x: null,
          y: null,
        };
        const res = user ? await CommentService.create(data) : await CommentService.createCommentSigner(currentSignerToken, data);
        const newData = Object.assign(res.data, {
          isRead: true,
          user: user ? user : null,
          signer: currentSigner ? currentSigner : null,
          isPosPDF: true
        });
        dispatch(setFocusComment(newData));
        if (location.search && location.search.indexOf('signerId') > -1) {
          setTabKey('comments');
        }
        setIsReply(false);
        if (res.data.parentId) {
          dispatch(addChildComment(newData));
        } else {
          dispatch(addComment(newData));
        }
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
      } finally {
        setIsReling(false);
      }
    }
  };

  const moveCommentPos = comment => {
    if (comment && comment.id) {
      const signPos = document.getElementById(`comment_${comment.id}`);
      if (signPos) {
        signPos.scrollIntoView({
          block: 'center',
        });
      }
    }
  };

  function onClickComment(comment) {
    moveCommentPos(comment);
    if (window.visualViewport.width < 992) {
      mouseOver(comment);
    }
  }

  const markNotification = async comment => {
    return new Promise(async (resolve, reject) => {
      try {
        if (user) {
          const noti = notifications.filter(item => {
            if (item.data.id == comment.id && !item.status) {
              return item;
            }
          });
          if (noti.length > 0) {
            await ContractService.markNotiContractUser(contract.id, noti[0].id);
            dispatch(updateNotifications({ id: noti[0].id }));
          }
        } else {
          const noti = notifications.filter(item => {
            if (item.data.id == comment.id && !item.status) {
              return item;
            }
          });
          if (noti.length > 0) {
            await ContractService.markNotiContractSigner(currentSignerToken, noti[0].id);
            dispatch(updateNotifications({ id: noti[0].id }));
          }
        }
        resolve(true);
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
      }
    });
  };

  const markedComment = async comment => {
    return new Promise(async (resolve, reject) => {
      try {
        if (user) {
          await CommentService.markCommentUser(comment.id);
          await markNotification(comment);
        } else {
          await CommentService.markCommentSigner(currentSignerToken, comment.id);
          await markNotification(comment);
        }
        dispatch(setFocusComment(null));
        dispatch(updateComments({ id: comment.id }));
        resolve(true);
      } catch (err) {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
      }
    });
  };

  async function mouseOver(comment) {
    let el;
    if (window.visualViewport.width < 992) {
      const contain = document.getElementById('mobileComments');
      if (contain) {
        el = contain.querySelector(`.comment_item.comment_item_${comment.id}`);
      }
    } else {
      el = document.getElementById(`comment_item_${comment.id}`);
    }
    if (!comment.isRead && el.classList.contains('new')) {
      el.classList.remove('new');
      await markedComment(comment);
    }
  }

  const preventText = event => {
    if (event.target.innerText.length === 512 && event.keyCode != 8) {
      event.preventDefault();
    }
  };

  const pasteContent = event => {
    event.preventDefault();
    var text = event.clipboardData.getData('text/plain');
    setContentComment(text);
    document.execCommand('insertHTML', false, text);
  };

  const counterLetter = e => {
    const textArea = e.target;
    const parent = textArea.parentNode.parentNode;
    // parent.scrollIntoView();
    if (textareaRef.current && textareaRef.current.innerText) {
      const letters = textareaRef.current.innerText.trim();
      setContentComment(letters.trim());
      setCounter(512 - letters.length);
    } else {
      setCounter(512);
      setContentComment('');
    }
  };
  const onCancelReply = () => {
    setIsReply(false);
    setCounter(512);
  };

  useEffect(() => {
    if (isReply) {
      const replyEl = window.visualViewport.width >= 992 ? document.querySelector(`#docsignTabContent .comment_item_${comment.id} .comment_reply .comment_text`) : document.querySelector(`#mobileComments .comment_item_${comment.id} .comment_reply .comment_text`);
      const replyRect = replyEl.getBoundingClientRect();
      const wrapper = window.visualViewport.width >= 992 ? document.querySelector('#docsignTabContent .simplebar-content-wrapper') : document.querySelector('#mobileComments_Wrapper .simplebar-content-wrapper');
      const wrapperRect = wrapper.getBoundingClientRect();
      if (replyRect.y + replyRect.height - wrapperRect.y - wrapperRect.height > 0) {
        wrapper.scrollTop = wrapper.scrollTop + (replyRect.y + replyRect.height - wrapperRect.y - wrapperRect.height);
      }
    }
  }, [isReply]);

  const onsFucusReply = (() => {
    setTimeout(() => {
      if (textareaRef) {
        textareaRef.current.focus();
      }
    }, 50);
  })

  return (
    <>
      {comment && (
        <div onClick={() => onClickComment(comment)} onMouseOver={() => mouseOver(comment)} id={`comment_item_${comment.id}`} className={`comment_item comment_item_${comment.id} ${!comment.isRead ? 'new' : ''}`}>
          <div className="row px-3 pt-2">
            <div className={`col-auto ${comment.parentId ? 'px-0' : 'pr-0 pl-2'}`}>
              <div className="person_img_wrap">
                {user && !comment.user && !comment.signer ? (
                  <>
                    {user.avatarPath ? (
                      <img
                        src={user.avatarPath ? user.avatarPath : ''}
                        className="rounded-circle person_img"
                        onError={e => {
                          e.target.onerror = null;
                          e.target.remove();
                        }}
                        alt=""
                        loading="lazy"
                      />
                    ) : (
                      <div className="img_alt">
                        <span>{GeneralService.getLetterName(user.fullName)}</span>
                      </div>
                    )}
                  </>
                ) : (
                  <>
                    {comment.user && comment.user.id ? (
                      <>
                        {comment.user && comment.user.avatarPath ? (
                          <img
                            src={comment.user && comment.user.avatarPath ? comment.user.avatarPath : ''}
                            className="rounded-circle person_img"
                            onError={e => {
                              e.target.onerror = null;
                              e.target.remove();
                            }}
                            alt=""
                            loading="lazy"
                          />
                        ) : (
                          <div className="img_alt">
                            <span>{GeneralService.getLetterName(comment.user.fullName)}</span>
                          </div>
                        )}
                      </>
                    ) : (
                      <>
                        {comment.signer && comment.signer.avatarPath ? (
                          <img
                            src={comment.signer && comment.signer.avatarPath ? comment.signer.avatarPath : ''}
                            className="rounded-circle person_img"
                            onError={e => {
                              e.target.onerror = null;
                              e.target.remove();
                            }}
                            alt=""
                            loading="lazy"
                          />
                        ) : (
                          <div className="img_alt">
                            <span>{GeneralService.getLetterName(comment.signer ? comment.signer.fullName : '')}</span>
                          </div>
                        )}
                      </>
                    )}
                  </>
                )}
              </div>
              {!child && (
                <div className="comment_view">
                  <IconChatYellow />
                  <span>{index + 1}</span>
                </div>
              )}
            </div>
            <div className="col px-2 comment_text">
              <div>
                {user && !comment.user && !comment.signer ? (
                  <>
                    <div className="person_name">
                      Tôi
                      {user.fullName}
                    </div>
                  </>
                ) : (
                  <div className="person_name">{comment.user ? (user && user.id == comment.user.id ? 'Tôi' : comment.user.fullName) : currentSigner && currentSigner.id == comment.signer.id ? 'Tôi' : comment.signer.fullName}</div>
                )}
                <div className={`text-editable`} contentEditable="true" dangerouslySetInnerHTML={{ __html: comment.content }}></div>
                {/* <div className="text-pre-line">{comment.content}</div> */}
              </div>
              <div className="comment_meta">
                <span>{getDate(comment.createdTime)}</span>&ensp;
                <a
                  href="javascript:void(0)"
                  onClick={e => {
                    e.preventDefault();
                    setIsReply(true);
                    onsFucusReply();
                  }}
                >
                  Trả lời
                </a>
              </div>
              {isReply && !comment.parentId && (
                <>
                  <div className="comment_reply">
                    <form className="row px-3 py-2" onSubmit={e => submitReplyComment(e)}>
                      <div className="col-auto pr-2 pl-0">
                        <div className="person_img_wrap">
                          {currentSigner.avatarPath ? (
                            <img
                              src={currentSigner.avatarPath ? currentSigner.avatarPath : ''}
                              className="rounded-circle person_img"
                              onError={e => {
                                e.target.onerror = null;
                                e.target.remove();
                              }}
                              alt=""
                              loading="lazy"
                            />
                          ) : (
                            <div className="img_alt">
                              <span>{GeneralService.getLetterName(currentSigner.fullName)}</span>
                            </div>
                          )}
                        </div>
                      </div>
                      <div className="col pl-2 pr-2 comment_text">
                        <div className="wrap-typing">
                          {
                            user && !comment.user && !comment.signer ? (
                              <div onPaste={e => pasteContent(e)} onKeyDown={e => preventText(e)} onInput={e => counterLetter(e)} ref={textareaRef} placeholder={`Trả lời ${user.fullName}`} contentEditable="true" className={`input-editable ${contentComment.length > 512 ? 'is-invalid' : ''}`}></div>
                            ) : (
                              // <textareaa onChange={counterLetter} maxLength="512" className="form-control overflow-hidden" ref={textareaRef} placeholder={`Trả lời ${user.fullName}`}></textareaa>
                              <div
                                onPaste={e => pasteContent(e)}
                                onKeyDown={e => preventText(e)}
                                onInput={e => counterLetter(e)}
                                ref={textareaRef}
                                placeholder={`Trả lời ${comment.user ? comment.user.fullName : comment.signer.fullName}`}
                                contentEditable="true"
                                className={`input-editable ${contentComment.length > 512 ? 'is-invalid' : ''}`}
                              ></div>
                            )
                            // <textareaa onChange={counterLetter} maxLength="512" className="form-control overflow-hidden" ref={textareaRef} placeholder={`Trả lời ${comment.user ? comment.user.fullName : comment.signer.fullName}`}></textareaa>
                          }
                          {counter < 512 && (
                            <span id="count" className="counter">
                              ({counter})
                            </span>
                          )}
                        </div>
                        <div className="comment_btn">
                          <button type="submit" disabled={isReling || contentComment.length == 0} className="btn btn_main">
                            {isReling ? (
                              <>
                                <Spinner /> Loading...{' '}
                              </>
                            ) : (
                              'Gửi đi'
                            )}
                          </button>
                          &nbsp;
                          <button type="button" className="btn btn_outline_main" onClick={e => onCancelReply(e)}>
                            Hủy
                          </button>
                        </div>
                      </div>
                    </form>
                  </div>
                </>
              )}
              {comment.childCommentDtos && comment.childCommentDtos.length > 0 && <ListComment contract={contract} comments={comment.childCommentDtos} child={true} currentSignerToken={currentSignerToken} />}
            </div>
          </div>
          {isReply && comment.parentId && (
            <>
              <div className="comment_reply">
                <form className="row px-3 py-2" onSubmit={e => submitReplyComment(e)}>
                  <div className={`col-auto ${comment.parentId ? 'px-0' : 'pr-0 pl-2'}`}>
                    <div className="person_img_wrap">
                      {currentSigner.avatarPath ? (
                        <img
                          src={currentSigner.avatarPath ? currentSigner.avatarPath : ''}
                          className="rounded-circle person_img"
                          onError={e => {
                            e.target.onerror = null;
                            e.target.remove();
                          }}
                          alt=""
                          loading="lazy"
                        />
                      ) : (
                        <div className="img_alt">
                          <span>{GeneralService.getLetterName(currentSigner.fullName)}</span>
                        </div>
                      )}
                    </div>
                  </div>
                  <div className="col pl-2 pr-2 comment_text">
                    <div className="wrap-typing">
                      {
                        user && !comment.user && !comment.signer ? (
                          <div onPaste={e => pasteContent(e)} onKeyDown={e => preventText(e)} onInput={e => counterLetter(e)} ref={textareaRef} placeholder={`Trả lời ${user.fullName}`} contentEditable="true" className={`input-editable ${contentComment.length > 512 ? 'is-invalid' : ''}`}></div>
                        ) : (
                          // <textareaa onChange={counterLetter} maxLength="512" className="form-control overflow-hidden" ref={textareaRef} placeholder={`Trả lời ${user.fullName}`}></textareaa>
                          <div
                            onPaste={e => pasteContent(e)}
                            onKeyDown={e => preventText(e)}
                            onInput={e => counterLetter(e)}
                            ref={textareaRef}
                            placeholder={`Trả lời ${comment.user ? comment.user.fullName : comment.signer.fullName}`}
                            contentEditable="true"
                            className={`input-editable ${contentComment.length > 512 ? 'is-invalid' : ''}`}
                          ></div>
                        )
                        // <textareaa onChange={counterLetter} maxLength="512" className="form-control overflow-hidden" ref={textareaRef} placeholder={`Trả lời ${comment.user ? comment.user.fullName : comment.signer.fullName}`}></textareaa>
                      }
                      {counter < 512 && (
                        <span id="count" className="counter">
                          ({counter})
                        </span>
                      )}
                    </div>
                    <div className="comment_btn">
                      <button type="submit" disabled={isReling || contentComment.length == 0} className="btn btn_main">
                        {isReling ? (
                          <>
                            <Spinner /> Loading...{' '}
                          </>
                        ) : (
                          'Gửi đi'
                        )}
                      </button>
                      &nbsp;
                      <button type="button" className="btn btn_outline_main" onClick={e => onCancelReply(e)}>
                        Hủy
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </>
          )}
        </div>
      )}
    </>
  );
};

const ListComment = ({ currentPage, onPageChanged, comments, contract, child = false, currentSignerToken, setTabKey }) => {
  return comments.length > 0 ? comments.map((c, index) => <Comment setTabKey={setTabKey} currentPage={currentPage} onPageChanged={onPageChanged} key={`comment_${index}`} comment={c} index={index} contract={contract} currentSignerToken={currentSignerToken} child={child}></Comment>) : null;
};

const Comments = ({ currentPage, onPageChanged, setTabKey }) => {
  const { contract, currentSigner, comments, newComment, focusComment, focusRealtimeComment, currentSignerToken } = useSelector(state => state.detailContract);
  const dispatch = useDispatch();
  const location = useLocation();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [counter, setCounter] = useState(512);
  const newCommentContent = useRef();
  const [isLoading, setIsLoading] = useState(false);
  const [contentComment, setContentComment] = useState('');
  const user = useSelector(state => state.auth.user);

  const submitNewComment = async event => {
    event.preventDefault();
    if (newCommentContent.current.innerText.length < 513) {
      try {
        setIsSubmitting(true);
        const percentPos = commentPositionPercent(newComment.page, newComment.x, newComment.y);
        const data = {
          contractId: contract.id,
          content: newCommentContent.current.innerText.substring(0, 511),
          parentId: null,
          x: percentPos.percentX,
          y: percentPos.percentY,
          page: newComment.page,
        };
        const res = user ? await CommentService.create(data) : await CommentService.createCommentSigner(currentSignerToken, data);
        const newData = Object.assign(res.data, {
          isRead: true,
          user: user ? user : null,
          signer: currentSigner ? currentSigner : null,
          childCommentDtos: [],
          isPosPDF: true
        });
        dispatch(setFocusComment(newData));
        dispatch(setNewComment(null));
        if (location.search && location.search.indexOf('signerId') > -1) {
          setTabKey('comments');
        }
        if (res.data.parentId) {
          dispatch(addChildComment(newData));
        } else {
          dispatch(addComment(newData));
        }
      } catch (err) {
        console.error(err);
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const cancelNewComment = () => {
    dispatch(setNewComment(null));
    setCounter(512);
  };

  function moveCommentFromLink(dataComments) {
    const arr = location.search.split('=');
    if (arr.length > 1) {
      for (const item of dataComments) {
        if (item.id == arr[1]) {
          dispatch(setFocusComment(item));
          if (item.page != currentPage) {
            dispatch(setPdfCurrentPage(item.page));
            dispatch(setPreviewCurrentPage(item.page));
          }
          return;
        }
        if (item.childCommentDtos && item.childCommentDtos.length > 0) {
          for (const child of item.childCommentDtos) {
            if (child.id == arr[1]) {
              dispatch(setFocusComment(child));
              if (child.page != currentPage) {
                dispatch(setPdfCurrentPage(child.page));
                dispatch(setPreviewCurrentPage(child.page));
              }
              return;
            }
          }
        }
      }
    }
  }

  const getComments = async () => {
    try {
      setIsLoading(true);
      const res = user ? await CommentService.list(contract.id) : await CommentService.listCommentSigner(currentSignerToken);
      dispatch(setComments(convertComments(res.data)));
      if (location.search && !focusComment) {
        moveCommentFromLink(res.data);
      }
      if (focusComment) {
        onMoveFocusComment();
      }
      setIsLoading(false);
    } catch (err) {
      setIsLoading(false);
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, contractName: contract.title }));
    }
  };

  const convertComments = comments => {
    return comments.map(cmt => {
      if (cmt.childCommentDtos) {
        return {
          ...cmt,
          ...{ childCommentDtos: convertComments(cmt.childCommentDtos) },
        };
      } else {
        return {
          ...cmt,
          ...{ childCommentDtos: [] },
        };
      }
    });
  };
  const onMoveFocusComment = () => {
    if (focusComment) {
      setTimeout(() => {
        const eleComment = window.visualViewport.width >= 992 ? document.querySelector(`#docsignTabContent .comment_item.comment_item_${focusComment.id}`) : document.querySelector(`#mobileComments .comment_item.comment_item_${focusComment.id}`);
        if (eleComment) {
          eleComment.scrollIntoView({ block: 'center' });
        }
      }, 100);
    }
  };

  const counterLetter = event => {
    // if (event.target.innerText.length === 512 && event.keyCode != 8) {
    //   event.preventDefault();
    // }
    const textArea = event.target;
    // textArea.style.height = "auto";
    // textArea.style.height = textArea.scrollHeight + "px";
    const btnArea = textArea.parentNode.parentNode.querySelector('.comment_btn');
    btnArea.scrollIntoView();
    if (newCommentContent.current && newCommentContent.current.innerText) {
      const letters = newCommentContent.current.innerText.trim();
      setContentComment(letters.trim());
      setCounter(512 - letters.length);
    } else {
      setCounter(512);
      setContentComment('');
    }
  };
  const preventText = event => {
    if (event.target.innerText.length === 512 && event.keyCode != 8) {
      event.preventDefault();
    }
  };

  const pasteContent = event => {
    event.preventDefault();
    var text = event.clipboardData.getData('text/plain');
    setContentComment(text);
    document.execCommand('insertHTML', false, text);
  };

  useEffect(() => {
    if (!comments || comments.length == 0) {
      getComments();
    }
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (focusComment) {
      setTimeout(() => {
        onMoveFocusComment();
      }, 100);
    }
  }, [focusComment]);

  useEffect(() => {
    const eleComment = window.visualViewport.width >= 992 ? document.querySelector(`#docsignTabContent .comment_item.comment_item_create`) : document.querySelector(`#mobileComments_Wrapper .comment_item.comment_item_create`);
    if (eleComment) {
      setTimeout(() => {
        eleComment.scrollIntoView();
      }, 100);
    }
  }, [newComment]);

  useEffect(() => {
    onMoveFocusComment();
  }, [comments])

  useEffect(() => {
    if (newComment) {
      setTimeout(() => {
        if (newCommentContent) {
          newCommentContent.current.focus();
        }
      }, 50);
    }
  }, [newComment]);

  return (
    <>
      {
        isLoading ? <>
          <div className="p-2">
            <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
          </div>
        </> :
          <>
            <ListComment currentPage={currentPage} onPageChanged={onPageChanged} comments={comments} contract={contract} setTabKey={setTabKey} currentSignerToken={currentSignerToken} />
            {newComment ? (
              <>
                <div className="comment_item comment_item_create">
                  <form className="row px-3 pt-2" onSubmit={e => submitNewComment(e)}>
                    <div className={`col-auto ${'pr-0 pl-2'}`}>
                      <div className="person_img_wrap">
                        {currentSigner.avatarPath ? (
                          <img
                            src={currentSigner.avatarPath ? currentSigner.avatarPath : ''}
                            className="rounded-circle person_img"
                            onError={e => {
                              e.target.onerror = null;
                              e.target.remove();
                            }}
                            alt=""
                            loading="lazy"
                          />
                        ) : (
                          <div className="img_alt">
                            <span>{GeneralService.getLetterName(currentSigner.fullName)}</span>
                          </div>
                        )}
                      </div>
                      <div className="comment_view">
                        <IconChatYellow />
                        <span>{comments.length + 1}</span>
                      </div>
                    </div>
                    <div className="col px-2 comment_text">
                      <div className="wrap-typing">
                        {/* <textareaa onChange={(e) => counterLetter(e)} maxLength="512" className="form-control overflow-hidden" style={{ overflowY: "hidden" }} placeholder="Viết bình luận" ref={newCommentContent} >
                  </textareaa> */}
                        <div onPaste={e => pasteContent(e)} onKeyDown={e => preventText(e)} onInput={e => counterLetter(e)} ref={newCommentContent} placeholder="Viết bình luận" contentEditable="true" className={`input-editable ${contentComment.length > 512 ? 'is-invalid' : ''}`} maxlength="512"></div>
                        {counter < 512 && (
                          <span id="count" className="counter">
                            ({counter})
                          </span>
                        )}
                      </div>
                      <div className="comment_btn">
                        <button type="submit" disabled={isSubmitting || contentComment.length == 0} className="btn btn_main">
                          {isSubmitting ? (
                            <>
                              <Spinner /> Đang gửi...{' '}
                            </>
                          ) : (
                            'Gửi đi'
                          )}
                        </button>
                        &nbsp;
                        <button type="button" className="btn btn_outline_main" onClick={e => cancelNewComment(e)}>
                          Hủy
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              </>
            ) : (
              ''
            )}
          </>
      }
    </>
  );
};
export default Comments;
