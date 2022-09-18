/* eslint-disable react-hooks/exhaustive-deps */
import { IconChatRightText, IconChatYellow, IconInfoCircle, IconLeftAngle, IconPencil, IconPeople, IconRightAngle } from '@Components/Icon';
import { Comments, ContractDetailInformation, SignersInformation } from '@Containers/Contract/ContractDetail';
import { TagModal } from '@Containers/Tag';
import { setFocusComment, setNewComment, setPdfCurrentPage, setPdfTotalPages, setPreviewCurrentPage, setShowSignerModal, setStartComment, setLoadedAllPageDetail, setFocusSign, setFocusRealtimeComment } from '@Redux/Actions/DetailContract';
import React, { useEffect, useRef, useState } from 'react';
import { Modal } from 'react-bootstrap';
import { Document, Page, pdfjs } from 'react-pdf';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';
import SimpleBar from 'simplebar-react';
import { checkDevice } from '../../../../Utils/helpers';
import ToolBar from '../Toolbar';
import styles from './index.module.scss';
pdfjs.GlobalWorkerOptions.workerSrc = `https://cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

const ContractDetailPDF = ({ getContracts, setTabKey, ...props }) => {
  const {
    contract,
    comments,
    newComment,
    startComment,
    focusComment,
    focusSign,
    currentSigner,
    currentSignerToken,
    isShowSignerModal,
    focusRealtimeComment,
    isLoadedAllPageDetail,
    pdf: { currentPage, totalPages },
    preview,
  } = useSelector(state => state.detailContract);
  const { user } = useSelector(state => state.auth);
  const location = useLocation();
  const dispatch = useDispatch();
  const [showTagModal, setShowTagModal] = useState(false);
  const [width, setWidth] = useState(830);
  const [showCommentModal, setShowCommentModal] = useState(false);
  const [showInfoModal, setShowInfoModal] = useState(false);
  // const [showSignerModal, setShowSignerModal] = useState(false);
  const [loaded, setLoaded] = useState(false);

  const previewRef = useRef(preview);
  const simpleBarRef = useRef({});
  const overlayRef = useRef({});
  const pageRefs = useRef({});
  const inputRef = useRef({});

  function onDocumentLoadSuccess({ numPages }) {
    dispatch(setPdfTotalPages(numPages));
    const el = overlayRef.current;
    if (!checkDevice()) {
      setWidth(el.offsetWidth - 33);
    } else {
      setWidth(el.offsetWidth - 18);
    }
    setLoaded(true);
  }

  function onDocumentLoadError() {
    setLoaded(true);
  }

  function onSubmitPage(e) {
    e.preventDefault();
    const page = parseInt(inputRef.current.value);
    if (currentPage != page) {
      dispatch(setPdfCurrentPage(page));
      dispatch(setPreviewCurrentPage(page));
    }
  }

  function goNextPage() {
    dispatch(setPdfCurrentPage(currentPage + 1));
    dispatch(setPreviewCurrentPage(currentPage + 1));
  }

  function goPreviousPage() {
    dispatch(setPdfCurrentPage(currentPage - 1));
    dispatch(setPreviewCurrentPage(currentPage - 1));
  }

  function onOverlayScroll() {
    const page = getCurrentPage();
    if (page != previewRef.current.currentPage) {
      dispatch(setPreviewCurrentPage(page));
      dispatch(setPdfCurrentPage(page));
    }
  }

  const onAttachTagSelected = () => {
    setShowTagModal(true);
  };

  function getCurrentPage() {
    const container = overlayRef.current;
    if (container) {
      let currentPage = 1;
      Object.keys(pageRefs.current).forEach((pageNumber, index) => {
        const ele = pageRefs.current[`${pageNumber}`];
        if (container.scrollTop + 10 >= ele.offsetTop) {
          currentPage = index + 1;
        }
      });
      return currentPage;
    }
  }

  const handleClose = () => {
    setShowTagModal(false);
  };

  function moveToCurrentPage() {
    const element = pageRefs.current[`${currentPage}`];
    const container = overlayRef.current;
    if (element) {
      if (isElementOutsideView(element, container)) {
        container.scrollTo(0, element.offsetTop);
      }
    }
  }

  function isElementOutsideView(element, container) {
    if (element.offsetTop > container.scrollTop) {
      return true;
    }
    if (element.offsetTop < container.scrollTop - element.offsetHeight) {
      return true;
    }
    return false;
  }

  function onClickPage(e, pageNumber) {
    if (startComment) {
      const page = pageRefs.current[`${pageNumber}`];
      const pageRect = page.getBoundingClientRect();
      const canvas = page.querySelector('canvas');
      const rect = canvas.getBoundingClientRect();
      const item = {
        page: pageNumber,
        x: e.pageX - Math.floor(rect.x) + (Math.floor(rect.x) - Math.floor(pageRect.x)),
        y: e.pageY - Math.floor(rect.y),
      };
      if (item.x < Math.floor(rect.x) - Math.floor(pageRect.x) - 1 || item.x + 28 > canvas.offsetWidth + Math.floor(rect.x) - Math.floor(pageRect.x) || item.y < 0 || item.y + 28 > canvas.offsetHeight) return;

      dispatch(
        setNewComment({
          ...item,
          ...{ content: '' },
        }),
      );
      dispatch(setStartComment(false));
    }
  }

  function onClickSigner(e, id) {
    e.preventDefault();
    e.stopPropagation();
    setTabKey('people');
    if (!isShowSignerModal && window.visualViewport.width < 992) {
      dispatch(setShowSignerModal(true));
      // setShowSignerModal(true);
      setTimeout(() => {
        const modalSigners = document.getElementById('mobileSigners');
        if (modalSigners) {
          const inforSigner = modalSigners.querySelector(`.inforSigner_${id}`);
          if (inforSigner) {
            inforSigner.scrollIntoView({
              block: 'center',
            });
          }
        }
      }, 100);
    } else {
      const inforSigner = document.querySelector(`.inforSigner_${id}`);
      if (inforSigner) {
        inforSigner.scrollIntoView({
          block: 'center',
        });
      }
    }
  }

  function onLoadedPage(e, pageIndex) {
    if (pageIndex + 1 == totalPages) {
      dispatch(setLoadedAllPageDetail(true));
      setTimeout(() => {
        if (focusComment && !showCommentModal && window.visualViewport.width < 992) {
          // setShowCommentModal(true);
        } else if (focusComment) {
          const commentPreview = document.getElementById(`comment_${focusComment.parentId ? focusComment.parentId : focusComment.id}`);
          if (commentPreview) {
            commentPreview.scrollIntoView({
              block: 'center',
            });
          }
        }
        if (location.search) {
          const arr = location.search.split('=');
          if (location.search.indexOf('signerId') > -1) {
            if (arr.length > 1 && focusSign) {
              setTimeout(() => {
                const signPos = document.getElementById(`signDetail_${arr[1]}`);
                if (signPos) {
                  signPos.scrollIntoView({
                    block: 'center',
                  });
                }
                setTabKey('people');
                const inforSigner = document.querySelector(`.inforSigner_${arr[1]}`);
                if (inforSigner) {
                  inforSigner.scrollIntoView({
                    block: 'center',
                  });
                }
              }, 100);
            }
          } else {
            const arr = location.search.split('=');
            if (arr.length > 1) {
              const posComment = document.getElementById(`comment_${arr[1]}`);
              if (posComment) {
                posComment.scrollIntoView({
                  block: 'center',
                });
              }
            }
          }
        }
      }, 100);
    }
  }

  function onMoveToSign(sign) {
    setTimeout(() => {
      const signPos = document.querySelector(`.signDetail_${sign.id}`);
      if (signPos) {
        signPos.scrollIntoView({
          block: 'center',
        });
      }
      setTabKey('people');
      const inforSigner = document.querySelector(`.inforSigner_${sign.id}`);
      if (inforSigner) {
        inforSigner.scrollIntoView({
          block: 'center',
        });
      }
    }, 100);
  }

  function onMoveToComment(comment) {
    if (window.visualViewport.width < 992) {
      if (!showCommentModal && comments && comments.length > 0) {
        setShowCommentModal(true);
      }
      if (comments && comments.length > 0) {
        setTimeout(() => {
          const contain = document.getElementById('mobileComments');
          const eleComment = contain.querySelector(`.comment_item.comment_item_${comment.parentId ? comment.parentId : comment.id}`);
          if (eleComment) {
            eleComment.scrollIntoView({
              block: 'center',
            });
          }
          if (!comment.latestRealtime && !comment.isPosPDF) {
            const posComment = document.getElementById(`comment_${comment.parentId ? comment.parentId : comment.id}`);
            if (posComment) {
              posComment.scrollIntoView({
                block: 'center',
              });
            }
          }
        }, 100);
      }
    } else {
      if (!comment.latestRealtime && !comment.isPosPDF) {
        setTimeout(() => {
          const posComment = document.getElementById(`comment_${comment.parentId ? comment.parentId : comment.id}`);
          if (posComment) {
            posComment.scrollIntoView({
              block: 'center',
            });
          }
        }, 100);
      }
    }
  }

  const hideModalComment = () => {
    setShowCommentModal(false);
    dispatch(setFocusComment(null));
  }
  const moveCommentFromPDF = (comment) => {
    if (focusComment && focusComment.id == comment.id) {
      dispatch(setFocusComment(null));
      setTimeout(() => {
        dispatch(setFocusComment(comment));
      }, 100);
    } else {
      dispatch(setFocusComment(comment));
    }
  }

  useEffect(() => {
    inputRef.current.value = currentPage;
    moveToCurrentPage();
  }, [currentPage]);

  useEffect(() => {
    if (!!focusSign) {
      onMoveToSign(focusSign);
    }
  }, [focusSign]);

  useEffect(() => {
    if (isLoadedAllPageDetail && !!focusSign) {
      setTimeout(() => {
        onMoveToSign(focusSign);
      }, 100);
    }
  }, [isLoadedAllPageDetail]);

  useEffect(() => {
    if (!!focusComment) {
      onMoveToComment(focusComment);
    }
  }, [focusComment]);

  useEffect(() => {
    if (!!focusComment && !newComment) {
      onMoveToComment(focusComment);
    }

  }, [comments]);

  useEffect(() => {
    if (newComment && !showCommentModal && window.visualViewport.width < 992) {
      setShowCommentModal(true);
      setTimeout(() => {
        const contain = document.getElementById('mobileComments');
        const eleNew = contain.querySelector(`.comment_item.comment_item_create`);
        if (eleNew) {
          eleNew.scrollIntoView({
            block: 'center',
          });
        }
      }, 100);
    }
  }, [newComment]);

  useEffect(() => {
    previewRef.current = preview;
  }, [preview]);

  useEffect(() => {
    overlayRef.current.removeEventListener('scroll', null);
    overlayRef.current.addEventListener('scroll', onOverlayScroll);
  }, []);

  return (
    <>
      <div className="preview_nav d-flex align-items-center row">
        <form className="col-auto pages_nums form-inline text-left d-none d-sm-inline-block" onSubmit={e => onSubmitPage(e)}>
          <span className="ml-2">
            Trang&nbsp;
            <input id="input-page" type="text" ref={inputRef} className="form-control form-control-sm text-center px-2 py-1 mx-1" defaultValue={currentPage} />/{totalPages}
          </span>
          <button type="submit" className="btn"></button>
        </form>
        <div className="col actions text-left text-sm-center pr-0">
          <ToolBar onAttachTagSelected={onAttachTagSelected}></ToolBar>
          <TagModal show={showTagModal} contracts={[contract]} onHide={handleClose}></TagModal>
        </div>
        <div className="col-auto nav_btn ml-auto text-right d-none d-lg-block">
          <button className={`btn ${styles.btnControlPage}`} style={{ marginRight: 5, cursor: 'pointer' }} type="button" title="Trang trước" disabled={currentPage <= 1} onClick={goPreviousPage}>
            <IconLeftAngle />
          </button>
          <button className={`btn ${styles.btnControlPage}`} type="button" style={{ cursor: 'pointer' }} title="Trang tiếp theo" disabled={currentPage == totalPages} onClick={goNextPage}>
            <IconRightAngle />
          </button>
        </div>
        <div className="col-auto nav_btn ml-auto text-right d-block d-lg-none pl-0">
          <a style={{ cursor: 'pointer' }} title="Thông tin" data-toggle="modal" data-target="#infoModal" onClick={() => setShowInfoModal(true)}>
            <IconInfoCircle />
          </a>
          <a style={{ cursor: 'pointer' }} title="Danh sách người nhận" data-toggle="modal" data-target="#peopleModal" onClick={() => dispatch(setShowSignerModal(true))}>
            <IconPeople />
          </a>
          <a style={{ cursor: 'pointer' }} title="Nhận xét" data-toggle="modal" data-target="#commentModal" onClick={() => setShowCommentModal(true)}>
            <IconChatRightText />
          </a>
        </div>
      </div>
      <div className={`preview_body p-2`}>
        <SimpleBar className="preview-pdf-overlay" id="showPDFSign" scrollableNodeProps={{ ref: overlayRef }} ref={simpleBarRef}>
          <Document loading={() => <div className="loader-pdf-sign"></div>} file={contract.fileUrl} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
            {loaded &&
              Array.from(new Array(totalPages), (el, pageIndex) => (
                <div key={`page_${pageIndex + 1}`} className="pdf-page-container" id={`pdf-detail-page-${pageIndex + 1}`} ref={ele => (pageRefs.current[`${pageIndex + 1}`] = ele)} style={{ position: 'relative' }}>
                  <Page onLoadSuccess={e => onLoadedPage(e, pageIndex)} loading={() => <div className="loader-pdf-sign"></div>} className={`pdf-page spliter-page ${startComment ? 'comment-icon' : ''}`} width={width} pageNumber={pageIndex + 1} onClick={e => onClickPage(e, pageIndex + 1)} />
                  {isLoadedAllPageDetail ? (
                    <>
                      {contract.signerDtos
                        .filter(item => item.page == pageIndex + 1)
                        .map(item => {
                          if (item.isSigned) {
                            return (
                              <React.Fragment key={`signer_${item.id}`}>
                                {item.email == (user ? user.email : currentSigner.email) ? (
                                  <div id={`signDetail_${item.email}`} className={`signature_inpreview me signed shadow-sm signDetail_${item.id}`} title="Đã ký" style={{ left: `${item.x}%`, top: `${item.y}%` }} onClick={e => onClickSigner(e, item.id)}>
                                    <div className="mx-auto">
                                      <div className="signer_name">{item.fullName}</div>
                                    </div>
                                  </div>
                                ) : (
                                  <div id={`signDetail_${item.email}`} className={`signature_inpreview signed shadow-sm signDetail_${item.id}`} title="Đã ký" style={{ left: `${item.x}%`, top: `${item.y}%` }} onClick={e => onClickSigner(e, item.id)}>
                                    <div className="mx-auto">
                                      <div className="signer_name">{item.fullName}</div>
                                    </div>
                                  </div>
                                )}
                              </React.Fragment>
                            );
                          } else {
                            return (
                              <React.Fragment key={`signer_${item.id}`}>
                                {item.email == (user ? user.email : currentSigner.email) ? (
                                  <div id={`signDetail_${item.email}`} className={`signature_inpreview  shadow-sm signDetail_${item.id}`} title="Chưa ký" style={{ left: `${item.x}%`, top: `${item.y}%` }} onClick={e => onClickSigner(e, item.id)}>
                                    <div className="mx-auto">
                                      <div className="signer_icon">
                                        <IconPencil />
                                      </div>
                                      <div className="signer_name">{item.fullName}</div>
                                    </div>
                                  </div>
                                ) : (
                                  <div id={`signDetail_${item.email}`} className={`signature_inpreview me  shadow-sm signDetail_${item.id}`} title="Chưa ký" style={{ left: `${item.x}%`, top: `${item.y}%` }} onClick={e => onClickSigner(e, item.id)}>
                                    <div className="mx-auto">
                                      <div className="signer_icon">
                                        <IconPencil />
                                      </div>
                                      <div className="signer_name">{item.fullName}</div>
                                    </div>
                                  </div>
                                )}
                              </React.Fragment>
                            );
                          }
                        })}
                      {comments
                        .filter(item => !!item.x)
                        .map((item, commentIndex) => (
                          <React.Fragment key={`comment_${item.id}`}>
                            {item.page == pageIndex + 1 && (
                              <div id={`comment_${item.id}`} className="comment_inpreview" style={{ top: `${item.y}%`, left: `${item.x}%` }} onClick={() => moveCommentFromPDF({ ...item, ...{ isPosPDF: true } })}>
                                <IconChatYellow width="32" height="32" />
                                <span>{commentIndex + 1}</span>
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                      {newComment && newComment.page == pageIndex + 1 && (
                        <>
                          <div className="comment_inpreview" style={{ top: newComment.y, left: newComment.x }}>
                            <IconChatYellow width="32" height="32" />
                            <span>{comments.length + 1}</span>
                          </div>
                        </>
                      )}
                    </>
                  ) : (
                    <></>
                  )}
                </div>
              ))}
          </Document>
        </SimpleBar>
        {/* </div> */}
      </div>
      <Modal aria-labelledby="ModalLabel" aria-hidden="true" className="docsign" dialogClassName="modal-dialog-centered modal-dialog-scrollable" show={showInfoModal} onHide={() => setShowInfoModal(false)}>
        <Modal.Header>
          <h5 className="modal-title">Thông tin</h5>
          <button type="button" className="close" aria-label="Close" onClick={() => setShowInfoModal(false)}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <SimpleBar className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags">
            <ContractDetailInformation />
          </Modal.Body>
        </SimpleBar>
      </Modal>

      <Modal aria-labelledby="ModalLabel" aria-hidden="true" className="docsign" dialogClassName="modal-dialog-centered modal-dialog-scrollable" show={isShowSignerModal} onHide={() => dispatch(setShowSignerModal(false))}>
        <Modal.Header>
          <h5 className="modal-title">Danh sách người nhận</h5>
          <button type="button" className="close" aria-label="Close" onClick={() => dispatch(setShowSignerModal(false))}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <SimpleBar className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags" id="mobileSigners">
            <SignersInformation stylePeople={props.stylePeople} />
          </Modal.Body>
        </SimpleBar>
      </Modal>

      <Modal aria-labelledby="ModalLabel" aria-hidden="true" className="docsign" dialogClassName="modal-dialog-centered modal-dialog-scrollable" show={showCommentModal} onHide={() => setShowCommentModal(false)}>
        <Modal.Header>
          <h5 className="modal-title">Nhận xét</h5>
          <button type="button" className="close" aria-label="Close" onClick={() => hideModalComment()}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <SimpleBar id="mobileComments_Wrapper" className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags" id="mobileComments">
            <Comments styleComment={props.styleComment} setTabKey={setTabKey} />
          </Modal.Body>
        </SimpleBar>
      </Modal>

      {/* <Modal aria-labelledby="ModalLabel" aria-hidden="true" className="docsign" dialogClassName="modal-dialog-centered modal-dialog-scrollable" show={showCommentModal} onHide={() => setShowCommentModal(false)}>
        <Modal.Header>
          <h5 className="modal-title">Nhận xét</h5>
          <button type="button" className="close" aria-label="Close" onClick={() => setShowCommentModal(false)}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        {!checkDevice() ? <SimpleBar className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags" id="mobileComments">
            <Comments styleComment={props.styleComment} />
          </Modal.Body>
        </SimpleBar> :
          <Modal.Body id="mobileComments">
            <Comments styleComment={props.styleComment} />
          </Modal.Body>
        }
      </Modal> */}

      {/* <Modal aria-labelledby="ModalLabel" aria-hidden="true" className="docsign" dialogClassName="modal-dialog-centered modal-dialog-scrollable" show={showCommentModal} onHide={() => setShowCommentModal(false)}>
        <Modal.Header>
          <h5 className="modal-title">Nhận xét</h5>
          <button type="button" className="close" aria-label="Close" onClick={() => setShowCommentModal(false)}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body id="mobileComments">
          <Comments styleComment={props.styleComment} />
        </Modal.Body>
      </Modal> */}
    </>
  );
};
export default ContractDetailPDF;
