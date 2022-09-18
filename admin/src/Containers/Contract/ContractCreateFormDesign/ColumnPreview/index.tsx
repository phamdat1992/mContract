import React, { useRef, useState } from 'react';
import { useEffect } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import { IconPencil } from '../../../../Components/Icon';
import styles from './index.module.scss';
import { useDispatch, useSelector } from 'react-redux';
import SimpleBar from 'simplebar-react';
import { setPdfCurrentPage, setPreviewCurrentPage, setPreviewTotalPages } from '@Redux/Actions/CreateContract';
import { checkDevice } from '../../../../Utils/helpers';
pdfjs.GlobalWorkerOptions.workerSrc = `https://cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

const ColumnPreview = () => {
  const { user } = useSelector((state: any) => state.auth);
  const {
    file,
    signers,
    preview: { currentPage, totalPages },
  } = useSelector((state: any) => state.createContract);
  const dispatch = useDispatch();
  const [width, setWidth] = useState(125);
  const [loaded, setLoaded] = useState(false);
  const overlayRef: any = useRef({});
  const previewRef: any = useRef({});
  const pageRefs: any = useRef({});

  function onDocumentLoadSuccess({ numPages }: any) {
    dispatch(setPreviewTotalPages(numPages));
    setLoaded(true);
  }

  function onDocumentLoadError() {
    setLoaded(true);
  }
  function onClickPage(pageNumber: number) {
    dispatch(setPreviewCurrentPage(pageNumber));
    dispatch(setPdfCurrentPage(pageNumber));
  }

  function onClickSigner(e: any, index: number) {
    e.preventDefault();
    e.stopPropagation();
    const signer: HTMLDivElement = document.getElementById(`right-item-${index}`) as HTMLDivElement;
    const container: HTMLDivElement = !checkDevice() ? document.querySelector('#showPDFCreate .simplebar-content-wrapper') as HTMLDivElement : document.querySelector('#showPDFCreate') as HTMLDivElement;
    if (signer && container) {
      const newTop = (signer.parentElement as HTMLDivElement).offsetTop + signer.offsetTop - (container.offsetHeight / 2) + (signer.offsetHeight / 2);
      container.scrollTop = newTop >= 0 ? newTop : 0;
    }
  }

  function moveToCurrentPage() {
    const target: any = pageRefs.current[`${currentPage}`];
    let container: any = null;
    if (!checkDevice()) {
      container = overlayRef.current;
    } else {
      container = document.querySelector('.create-contract-preview');
    }
    if (target && container) {
      container.scrollTo(0, target.offsetTop - (container.offsetHeight - target.offsetHeight - 36));
    }
  }

  useEffect(() => {
    moveToCurrentPage();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage]);

  return (
    <div className="col pages mt-3 pl-0 pr-2 d-none d-xl-block">
      <SimpleBar style={{ height: '100%', padding: '0 1rem' }} className="pdf-preview-overlay create-contract-preview" ref={previewRef} scrollableNodeProps={{ ref: overlayRef }}>
        <div className="pdf-spliter-container">
          <Document loading={() => <div className="loader-pdf-create-preview"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
            {loaded &&
              Array.from(new Array(totalPages)).map((el, index) => (
                <div key={`preview_pdf_${index + 1}`} className="pdf-page-container" id={`pdf-preview-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)}>
                  <Page loading="" className={`pdf-page page-item mb-3 ${styles.spliterPage} ${index + 1 == currentPage ? `${styles.page_active}` : ''}`} width={width} pageNumber={index + 1} onClick={() => onClickPage(index + 1)}>
                    <div className={styles.listSigner}>
                      {signers.map((item: any) => {
                        return (
                          <React.Fragment key={`preview_pdf_signer_icon_${item.index}`}>
                            {item.position.page == index + 1 && (
                              <div id={`preview-create-${item.index}`} onClick={(e) => onClickSigner(e, item.index)} className={`${styles.signature_icon} ${item.email == user.email ? '' : `${styles.signature_icon_me}`}`}>
                                <IconPencil />
                              </div>
                            )}
                          </React.Fragment>
                        );
                      })}
                    </div>
                  </Page>
                </div>
              ))}
          </Document>
        </div>
      </SimpleBar>

      {/* {!checkDevice() ? (
        <SimpleBar style={{ height: '100%', padding: '0 1rem' }} className="pdf-preview-overlay create-contract-preview" ref={previewRef} scrollableNodeProps={{ ref: overlayRef }}>
          <div className="pdf-spliter-container">
            <Document loading={() => <div className="loader-pdf-create-preview"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
              {loaded &&
                Array.from(new Array(totalPages)).map((el, index) => (
                  <div key={`preview_pdf_${index + 1}`} className="pdf-page-container" id={`pdf-preview-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)}>
                    <Page loading="" className={`pdf-page page-item mb-3 ${styles.spliterPage} ${index + 1 == currentPage ? `${styles.page_active}` : ''}`} width={width} pageNumber={index + 1} onClick={() => onClickPage(index + 1)}>
                      <div className={styles.listSigner}>
                        {signers.map((item: any) => {
                          return (
                            <React.Fragment key={`preview_pdf_signer_icon_${item.index}`}>
                              {item.position.page == index + 1 && (
                                <div id={`preview-create-${item.index}`} onClick={(e) => onClickSigner(e, item.index)} className={`${styles.signature_icon} ${item.email == user.email ? '' : `${styles.signature_icon_me}`}`}>
                                  <IconPencil />
                                </div>
                              )}
                            </React.Fragment>
                          );
                        })}
                      </div>
                    </Page>
                  </div>
                ))}
            </Document>
          </div>
        </SimpleBar>
      ) : (
        <div style={{ height: '100%', padding: '0 1rem' }} className="pdf-preview-overlay create-contract-preview" ref={previewRef}>
          <div className="pdf-spliter-container">
            <Document loading={() => <div className="loader-pdf-create"></div>} file={file.pdf.url} options={{ workerSrc: '/pdf.worker.js' }} onLoadSuccess={onDocumentLoadSuccess} onLoadError={onDocumentLoadError}>
              {loaded &&
                Array.from(new Array(totalPages)).map((el, index) => (
                  <div key={`preview_pdf_${index + 1}`} className="pdf-page-container" id={`pdf-preview-${index + 1}`} ref={ele => (pageRefs.current[`${index + 1}`] = ele)}>
                    <Page loading="" className={`pdf-page page-item mb-3 ${styles.spliterPage} ${index + 1 == currentPage ? `${styles.page_active}` : ''}`} width={width} pageNumber={index + 1} onClick={(e) => onClickPage(index + 1)}>
                      <div className={styles.listSigner}>
                        {signers.map((item: any) => {
                          return (
                            <React.Fragment key={`preview_pdf_signer_icon_${item.index}`}>
                              {item.position.page == index + 1 && (
                                <div className={`${styles.signature_icon} ${item.email == user.email ? '' : `${styles.signature_icon_me}`}`} onClick={(e) => onClickSigner(e, item.index)} >
                                  <IconPencil />
                                </div>
                              )}
                            </React.Fragment>
                          );
                        })}
                      </div>
                    </Page>
                  </div>
                ))}
            </Document>
          </div>
        </div>
      )} */}
    </div>
  );
};
export default ColumnPreview;
