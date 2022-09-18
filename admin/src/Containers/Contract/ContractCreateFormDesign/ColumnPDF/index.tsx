import React, { DragEvent, SyntheticEvent, useEffect, useRef, useState } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import { useDispatch, useSelector } from 'react-redux';
import SimpleBar from 'simplebar-react';
import { setPdfCurrentPage, setPdfTotalPages, setPreviewCurrentPage, updateSignerPosition } from '@Redux/Actions/CreateContract';
import { IconLeftAngle, IconPencil, IconSign, IconRightAngle } from '../../../../Components/Icon';
import styles from './index.module.scss';
import { checkDevice } from '../../../../Utils/helpers';
pdfjs.GlobalWorkerOptions.workerSrc = `https://cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

const ColumnPDF = ({ simplebarRef, setLoadedPDF }: any) => {
  const [loaded, setLoaded] = useState(false);
  const { user } = useSelector((state: any) => state.auth);
  const {
    file,
    signers,
    pdf: { currentPage, totalPages },
    preview,
  } = useSelector((state: any) => state.createContract);
  const dispatch = useDispatch();

  const [width, setWidth] = useState(681);
  const previewRef: any = useRef(preview);
  const pageRefs: any = useRef({});
  const inputRef: any = useRef({});

  function onDocumentLoadSuccess({ numPages }: any) {
    dispatch(setPdfTotalPages(numPages));
    setLoaded(true);
   
    const el = document.querySelector('.preview-pdf-overlay') as HTMLElement;
    if (checkDevice()) {
      setWidth(el.offsetWidth - 18);
    } else {
      setWidth(el.offsetWidth - 33);
    }
  }
  function onDocumentLoadError() {
    setLoaded(true);
  }

  function onSubmitPage(e: SyntheticEvent) {
    e.preventDefault();
    const page = parseInt(inputRef.current.value);
    if (currentPage != page) {
      dispatch(setPdfCurrentPage(page));
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
      dispatch(setPdfCurrentPage(page));
      dispatch(setPreviewCurrentPage(page));
    }
  }

  const onDrop = (e: DragEvent) => {
    const index = parseInt(e.dataTransfer.getData('index'));
    let draggableElement = e.target as HTMLElement;
    if (draggableElement.classList.contains('signature_inpreview')) {
      draggableElement = draggableElement.parentElement as HTMLElement;
    } else if (draggableElement.classList.contains('fn_close') || draggableElement.classList.contains('mx-auto')) {
      draggableElement = draggableElement.parentElement?.parentElement as HTMLElement;
    } else if (draggableElement.classList.contains('signer_name') || draggableElement.classList.contains('signer_icon')) {
      draggableElement = draggableElement.parentElement?.parentElement?.parentElement as HTMLElement;
    } else if (draggableElement.tagName == 'svg') {
      draggableElement = draggableElement.parentElement?.parentElement?.parentElement?.parentElement as HTMLElement;
    } else if (draggableElement.tagName == 'path') {
      draggableElement = draggableElement.parentElement?.parentElement?.parentElement?.parentElement?.parentElement as HTMLElement;
    }
    const draggableRect = draggableElement.getBoundingClientRect();
    const pageElement = draggableElement.querySelector('.pdf-page canvas') as HTMLElement;
    if (pageElement) {
      const pageRect = pageElement.getBoundingClientRect();
      const page = parseInt(draggableElement.getAttribute('data-page') as string);
      let x = e.clientX - draggableRect.x - Math.floor(draggableElement.offsetWidth * (10 / 100));
      let y = e.clientY - draggableRect.y - Math.floor(draggableElement.offsetHeight * (4.5 / 100));
      if (x < pageRect.x - draggableRect.x) x = pageRect.x - draggableRect.x;
      if (x > draggableRect.width - Math.floor(draggableRect.width * (1 / 5)) - (pageRect.x - draggableRect.x)) x = draggableRect.width - Math.floor(draggableRect.width * (1 / 5)) - (pageRect.x - draggableRect.x);
      if (y < 0) y = 0;
      if (y > draggableElement.scrollHeight - 89) y = draggableElement.scrollHeight - 89;

      const percentX = (x / draggableRect.width) * 100;
      const percentY = (y / draggableRect.height) * 100;

      const position = {
        page: page,
        x: x,
        y: y,
        percentX: percentX,
        percentY: percentY,
      };

      dispatch(
        updateSignerPosition({
          index: index,
          position: position,
        }),
      );
    }
  };

  const onDragOver = (e: DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const onDragStart = (e: DragEvent, index: number) => {
    e.dataTransfer?.setData('index', index.toString());
  };

  function getCurrentPage() {
    const pages = pageRefs.current;
    let page = 1;
    Object.keys(pages).forEach((pageNumber: string) => {
      const ele = pages[pageNumber];
      if (ele) {
        if (simplebarRef.current.contentWrapperEl.scrollTop + 10 >= ele.offsetTop) {
          page = parseInt(pageNumber);
        }
      }
    });

    return page;
  }

  function moveToCurrentPage() {
    const target: any = pageRefs.current[`${currentPage}`];
    let container: any;
    if (!checkDevice()) {
      container = simplebarRef.current.contentWrapperEl;
    } else {
      container = document.getElementById('showPDFCreate');
    }
    if (target && isElementOutsideView(target, container)) {
      container.scrollTo(0, target.offsetTop - 10);
    }
  }

  function isElementOutsideView(element: any, container: any) {
    if (element.offsetTop > container.scrollTop - 10) {
      return true;
    }
    if (element.offsetTop <= container.scrollTop - element.offsetHeight) {
      return true;
    }
    return false;
  }

  function resetSignerPosition(index: number) {
    dispatch(
      updateSignerPosition({
        index: index,
        position: {
          x: 0,
          y: 0,
          percentX: 0,
          percentY: 0,
          page: null,
        },
      }),
    );
  }
  function onLoadedPage(e: any, pageIndex: any) {
    if (pageIndex + 1 == totalPages) {
      setLoadedPDF(true);
    }
  }

  useEffect(() => {
    inputRef.current.value = currentPage;
    if (simplebarRef && simplebarRef.current) {
      moveToCurrentPage();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage]);

  useEffect(() => {
    previewRef.current = preview;
  }, [preview]);

  useEffect(() => {
    if (simplebarRef && simplebarRef.current) {
      if (simplebarRef.current.contentWrapperEl) {
        simplebarRef.current.contentWrapperEl.addEventListener('scroll', onOverlayScroll);
      }
      return () => {
        if (simplebarRef.current && simplebarRef.current.contentWrapperEl) {
          simplebarRef.current.contentWrapperEl.removeEventListener('scroll', null);
        }
      };
    }
  }, []);
  return (
    <div className="col-md-9 col-lg-10 col-xl-8 doc_preview px-2">
      <div className="preview_nav d-none d-md-flex mb-2 row">
        <form className="col pages_nums form-inline text-left d-inline-block" onSubmit={e => onSubmitPage(e)}>
          <span className="ml-2">
            Trang&nbsp;
            <input id="input-page" type="text" ref={inputRef} className="form-control form-control-sm text-center px-2 py-1 mx-1" defaultValue={currentPage} />/{totalPages}
          </span>
          <button type="submit" className="btn"></button>
        </form>
        <div className="col nav_btn ml-auto text-right">
          <button className={`btn ${styles.btnControlPage}`} type="button" title="Trang trước" disabled={currentPage <= 1} onClick={goPreviousPage}>
            <IconLeftAngle />
          </button>
          <button className={`btn ${styles.btnControlPage}`} type="button" title="Trang tiếp theo" disabled={currentPage == totalPages} onClick={goNextPage}>
            <IconRightAngle />
          </button>
        </div>
      </div>
      <div className="preview_body p-2 preview-contain">
        <SimpleBar className="preview-pdf-overlay" id="showPDFCreate" ref={simplebarRef}>
          <Document loading={() => <div className="loader-pdf-create"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
            {loaded &&
              Array.from(new Array(totalPages), (el, index) => (
                <div key={`page_${index + 1}`} className="pdf-page-container create-page-contain" data-page={index + 1} id={`pdf-page-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)} onDrop={(e: DragEvent) => onDrop(e)} onDragOver={(e: DragEvent) => onDragOver(e)}>
                  <Page loading="" onLoadSuccess={e => onLoadedPage(e, index)} className="pdf-page spliter-page" width={width} pageNumber={index + 1} />
                  {signers
                    .filter((signer: any) => signer.position?.page == index + 1)
                    .map((item: any, index: number) => {
                      return (
                        <div
                          className={`signature_inpreview right-item-${item.index} ${item.email == user.email ? '' : 'me'} shadow-sm draggable dropped`}
                          title={`${item.email == user.email ? 'Chữ ký của tôi' : `Chữ ký đối tác: ${item.fullName}`} `}
                          style={{ left: `${item.position.x}px`, top: `${item.position.y}px` }}
                          key={`right-item-${item.index}`}
                          id={`right-item-${item.index}`}
                          data-index={item.index}
                          onDragStart={(e: DragEvent) => onDragStart(e, item.index)}
                          draggable="true"
                        >
                          <div className="fn_close" title="Xóa" onClick={() => resetSignerPosition(item.index)}>
                            ×
                          </div>
                          <div className="mx-auto">
                            <div className="signer_icon">
                              <IconSign />
                            </div>
                            <div className="signer_name">{item.fullName}</div>
                          </div>
                        </div>
                      );
                    })}
                </div>
              ))}
          </Document>
        </SimpleBar>

        {/* {!checkDevice() ? (
          <SimpleBar className="preview-pdf-overlay" id="showPDFCreate" ref={simplebarRef}>
            <Document loading={() => <div className="loader-pdf-create"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
              {loaded &&
                Array.from(new Array(totalPages), (el, index) => (
                  <div key={`page_${index + 1}`} className="pdf-page-container create-page-contain" data-page={index + 1} id={`pdf-page-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)} onDrop={(e: DragEvent) => onDrop(e)} onDragOver={(e: DragEvent) => onDragOver(e)}>
                    <Page loading="" className="pdf-page spliter-page" width={width} pageNumber={index + 1} />
                    {signers
                      .filter((signer: any) => signer.position?.page == index + 1)
                      .map((item: any, index: number) => {
                        return (
                          <div
                            className={`signature_inpreview right-item-${item.index} ${item.email == user.email ? '' : 'me'} shadow-sm draggable dropped`}
                            title={`${item.email == user.email ? 'Chữ ký của tôi' : `Chữ ký đối tác: ${item.fullName}`} `}
                            style={{ left: `${item.position.x}px`, top: `${item.position.y}px` }}
                            key={`right-item-${item.index}`}
                            id={`right-item-${item.index}`}
                            data-index={item.index}
                            onDragStart={(e: DragEvent) => onDragStart(e, item.index)}
                            draggable="true"
                          >
                            <div className="fn_close" title="Xóa" onClick={() => resetSignerPosition(item.index)}>
                              ×
                            </div>
                            <div className="mx-auto">
                              <div className="signer_icon">
                                <IconSign />
                              </div>
                              <div className="signer_name">{item.fullName}</div>
                            </div>
                          </div>
                        );
                      })}
                  </div>
                ))}
            </Document>
          </SimpleBar>
        ) : (
          <div className="preview-pdf-overlay" id="showPDFCreate" ref={simplebarRef}>
            <Document loading={() => <div className="loader-pdf-create"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
              {loaded &&
                Array.from(new Array(totalPages), (el, index) => (
                  <div key={`page_${index + 1}`} className="pdf-page-container create-page-contain" data-page={index + 1} id={`pdf-page-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)} onDrop={(e: DragEvent) => onDrop(e)} onDragOver={(e: DragEvent) => onDragOver(e)}>
                    <Page loading="" className="pdf-page spliter-page" width={width} pageNumber={index + 1} />
                    {signers
                      .filter((signer: any) => signer.position?.page == index + 1)
                      .map((item: any, index: number) => {
                        return (
                          <div
                            className={`signature_inpreview right-item-${item.index} ${item.email == user.email ? '' : 'me'} shadow-sm draggable dropped`}
                            title={`${item.email == user.email ? 'Chữ ký của tôi' : `Chữ ký đối tác: ${item.fullName}`} `}
                            style={{ left: `${item.position.x}px`, top: `${item.position.y}px` }}
                            key={`right-item-${item.index}`}
                            id={`right-item-${item.index}`}
                            data-index={item.index}
                            onDragStart={(e: DragEvent) => onDragStart(e, item.index)}
                            draggable="true"
                          >
                            <div className="fn_close" title="Xóa" onClick={() => resetSignerPosition(item.index)}>
                              ×
                            </div>
                            <div className="mx-auto">
                              <div className="signer_icon">
                                <IconSign />
                              </div>
                              <div className="signer_name">{item.fullName}</div>
                            </div>
                          </div>
                        );
                      })}
                  </div>
                ))}
            </Document>
          </div>
        )} */}
      </div>
    </div>
  );
};
export default ColumnPDF;
