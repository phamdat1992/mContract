import React, { useEffect, useRef, useState } from 'react';
import { IconChatYellow } from '@Components/Icon';
import { Document, Page, pdfjs } from 'react-pdf';
import SimpleBar from 'simplebar-react';
import { IconCheck, IconPencil } from '@Components/Icon';
import styles from './index.module.scss';
import { useDispatch, useSelector } from 'react-redux';
import { setFocusComment, setPreviewCurrentPage, setPreviewTotalPages, setPdfCurrentPage, setLoadedAllPagePreviewDetail, setFocusSign } from '@Redux/Actions/DetailContract';
import { Spinner } from 'react-bootstrap';
pdfjs.GlobalWorkerOptions.workerSrc = `https://cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

const ContractDetailPreview = ({ setTabKey }) => {
  const {
    comments,
    currentSigner,
    contract,
    isLoadedAllPagePreviewDetail,
    currentSignerToken,
    preview: { currentPage, totalPages },
  } = useSelector(state => state.detailContract);
  const { user } = useSelector(state => state.auth);
  const dispatch = useDispatch();
  const [width, setWidth] = useState(73);

  const pageRefs = useRef({});
  const simplebarRef = useRef({});
  const overlayRef = useRef({});
  const [loaded, setLoaded] = useState(false);
  function onDocumentLoadSuccess({ numPages }) {
    dispatch(setPreviewTotalPages(numPages));
    if (overlayRef.current.offsetHeight > 383) {
      setWidth(overlayRef.current.offsetWidth - 35);
    } else {
      setWidth(overlayRef.current.offsetWidth - 19);
    }
    setLoaded(true);
  }

  function onDocumentLoadError() {
    setLoaded(true);
  }

  const moveCommentPos = comment => {
    const commentPDF = document.getElementById(`comment_${comment.id}`);
    const commentCol = document.getElementById(`comment_item_${comment.id}`);
    if (commentPDF) {
      commentPDF.scrollIntoView({
        block: 'center',
      });
    }

    if (commentCol) {
      commentCol.scrollIntoView({
        block: 'center',
      });
    }
  };
  function movePosition(comment) {
    dispatch(setFocusComment(comment));
    moveCommentPos(comment);
  }

  function onClickPage(pageNumber) {
    if (pageNumber != currentPage) {
      dispatch(setPreviewCurrentPage(pageNumber));
      dispatch(setPdfCurrentPage(pageNumber));
    }
  }

  function onClickSigner(e, id) {
    e.preventDefault();
    e.stopPropagation();
    setTabKey('people');
    const signPos = document.querySelector(`.signDetail_${id}`);
    if (signPos) {
      signPos.scrollIntoView({
        block: 'center',
      });
    }
    const inforSigner = document.querySelector(`.inforSigner_${id}`);
    if (inforSigner) {
      inforSigner.scrollIntoView({
        block: 'center',
      });
    }
  }

  function moveToCurrentPage() {
    const element = pageRefs.current[`${currentPage}`];
    const container = overlayRef.current;
    if (element && container) {
      container.scrollTo(0, element.offsetTop - (container.offsetHeight - element.offsetHeight - 36));
    }
  }
  function onLoadedPage(e, pageIndex) {
    if (pageIndex + 1 == totalPages) {
      dispatch(setLoadedAllPagePreviewDetail(true));
    }
  }
  useEffect(() => {
    moveToCurrentPage();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage]);

  return (
    <>
      {/* <div className="col-1 pages p-2 d-none d-lg-block pdf-preview-overlay" ref={simplebarRef} onScroll={onOverlayScroll}> */}
      <SimpleBar className={`${styles.detail_preview} col-1 pages p-2 d-none d-lg-block pdf-preview-overlay`} ref={simplebarRef} scrollableNodeProps={{ ref: overlayRef }}>
        <Document loading={() => <div className="loader-pdf-sign-preview"></div>} file={contract.fileUrl} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
          {loaded &&
            Array.from(new Array(totalPages), (el, pageIndex) => (
              <div key={`page_${pageIndex + 1}`} className="pdf-page-container" id={`pdf-detail-preview-${pageIndex + 1}`} ref={ele => (pageRefs.current[`${pageIndex + 1}`] = ele)}>
                <Page onLoadSuccess={e => onLoadedPage(e, pageIndex)} loading="" className={`pdf-page spliter-page-preview page-item ${styles.spliterPage} ${pageIndex + 1 == currentPage ? `${styles.page_active}` : ''}`} width={width} pageNumber={pageIndex + 1} onClick={() => onClickPage(pageIndex + 1)}>
                  {isLoadedAllPagePreviewDetail ? <>
                    <div className="si_area">
                      {contract.signerDtos.map(item => {
                        if (item.page == pageIndex + 1) {
                          return (
                            <React.Fragment key={`signer_${item.id}`}>
                              {item.isSigned ? (
                                <>
                                  <div onClick={e => onClickSigner(e, item.id)} className={`signature_icon signed ${item.email == (user ? user.email : currentSigner.email) ? '' : 'me'} `}>
                                    <IconCheck />
                                  </div>
                                </>
                              ) : (
                                <>
                                  <div onClick={e => onClickSigner(e, item.id)} className={`signature_icon ${item.email == (user ? user.email : currentSigner.email) ? '' : 'me'} `}>
                                    <IconPencil />
                                  </div>
                                </>
                              )}
                            </React.Fragment>
                          );
                        }
                        return <React.Fragment key={`signer_${item.id}`}></React.Fragment>;
                      })}
                    </div>
                    {comments &&
                      comments
                        .filter(item => !!item.x)
                        .map((item, commentIndex) => (
                          <React.Fragment key={`comment_${item.id}`}>
                            {item.page == pageIndex + 1 && (
                              <div className="comment_inpreview" style={{ top: item.y < 85 ? `${item.y}%` : '85%', left: item.x < 87 ? `${item.x}%` : '87%' }} onClick={() => movePosition(item)}>
                                <IconChatYellow width="12" height="12" />
                                <span style={{ fontSize: 6 }}>{commentIndex + 1}</span>
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                  </> : <></>}
                </Page>
              </div>
            ))}
        </Document>
      </SimpleBar>
      {/* </div> */}
    </>
  );
};
export default ContractDetailPreview;
