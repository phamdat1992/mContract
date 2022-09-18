import pdf from '@/assets/images/pdf.svg';
import word from '@/assets/images/word.webp';
import { IconUpload, IconOtherUpload } from '@Components/Icon';
import { setFile, setStep } from '@Redux/Actions/CreateContract';
import * as Axios from 'axios';
import { useCallback, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useDropzone } from 'react-dropzone';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';
import { setModal } from '@Redux/Actions/AlertModal';
import { addToast } from "@Redux/Actions/AlertToast";
const API_BASE_URL = 'https://mcontract.vn/api';

const ContractCreateFormFile = () => {
  const { file, currentStep } = useSelector(state => state.createContract);
  const dispatch = useDispatch();

  const [isRequesting, setIsRequesting] = useState(false);
  const { handleSubmit, setValue } = useForm({
    defaultValues: {
      file: file.origin ? file.origin : null,
    },
  });

  const getFile = formData => {
    return new Promise(async (resolve, reject) => {
      try {
        const res = await Axios.post(`${API_BASE_URL}/file-uploads/file-convert`, formData, {
          responseType: 'blob',
          crossDomain: true,
          withCredentials: true,
        });
        resolve(res.data);
      } catch (err) {
        reject(err);
      } finally {
        setIsRequesting(false);
      }
    });
  };

  const uploadFile = async file => {
    try {
      const docTypes = ['DOC', 'DOCX'];
      if (Math.floor((file.size / 1024) / 1024) > 200) {
        dispatch(setModal({ isShow: true, message: "Vui lòng không upload file quá 200MB!" }));
        return;
      }
      setIsRequesting(true);
      const splitFile = file.name.split('.');
      const fileType = splitFile[splitFile.length - 1];
      const fileName = splitFile[0];
      if (docTypes.indexOf(fileType.toUpperCase()) < 0) {
        setFiles(file, file, fileType, fileName);
      } else {
        const formData = new FormData();
        formData.append('file', file);
        const res = await getFile(formData);
        const arr = file.name.split('.');
        const newFile = new File([res], `${arr[0]}.pdf`, { type: 'application/pdf' });
        Object.assign(newFile, { path: `${arr[0]}.pdf` });
        setFiles(file, newFile, fileType, fileName);
      }
    } catch (err) {
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));

    } finally {
      setIsRequesting(false);
    }
  };

  const setFiles = (origin, pdf, fileType, fileName) => {
    dispatch(
      setFile({
        type: fileType,
        fileName: fileName,
        origin: {
          name: origin.name,
          path: origin.path,
          size: origin.size,
          uploadTime: new Date().getTime(),
          url: URL.createObjectURL(origin),
        },
        pdf: {
          name: pdf.name,
          path: pdf.path,
          size: pdf.size,
          uploadTime: new Date().getTime(),
          url: URL.createObjectURL(pdf),
        },
      }),
    );
  }

  const onDrop = useCallback(files => {
    if (files.length > 0) {
      uploadFile(files[0]);
    } else {
      setValue('file', null);
    }
    // eslint-disable-next-line
  }, []);

  const { getRootProps, getInputProps } = useDropzone({ maxFiles: 1, accept: '.doc, .docx,.pdf', onDrop });

  const onSubmit = () => {
    dispatch(
      setStep(2),
    );
  };

  const getFileDate = ms => {
    const date = new Date(ms);
    return `${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)} ${('0' + date.getDate()).slice(-2)}/${('0' + (date.getMonth() + 1)).slice(-2)}/${date.getFullYear()}`;
  };

  return (
    <>
      <div className="new_contract_body">
        <div className={`ncbody_wrap ncbody_wrap_step1 m-3 my-4 m-md-5 text-center ${file.origin ? 'has_ncnav' : ''}`}>
          <div className="top_border"></div>
          <div className="nc_body_content">
            <form onSubmit={handleSubmit(onSubmit)}>
              {!file.origin ? (
                <>
                  {isRequesting ? (
                    <>
                      {' '}
                      <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....{' '}
                    </>
                  ) : (
                    <div {...getRootProps()}>
                      <input hidden name="file" {...getInputProps()} type="file" />
                      <label style={{ cursor: 'pointer' }} className="mb-0">
                        <div className="mb-3 mb-sm-4 mt-4 mt-sm-5">
                          <IconUpload style={{ fontSize: '95px', color: '#2c62d6' }} />
                        </div>
                        <h4 style={{ fontStyle: 'italic', color: '#212529' }} className="font-weight-normal px-3 mb-4 mb-sm-5">
                          Kéo thả hoặc bấm vào đây để tải lên hợp đồng của bạn
                          <br />
                          (.doc, .docx, .pdf)
                        </h4>
                      </label>
                    </div>
                  )}
                </>
              ) : (
                <>
                  <div {...getRootProps()}>
                    {isRequesting ? (
                      <>
                        {' '}
                        <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....{' '}
                      </>
                    ) : (
                      <>
                        <div className="uploaded_file text-center text-md-left m-4 m-md-5">
                          <img className={`file_icon float-md-left mt-2 mt-md-0 mr-md-3 ${file.type == 'pdf' ? 'img-pdf' : 'img-word'}`} src={file.type == 'pdf' ? pdf : word} alt="" loading="lazy" />
                          <div className="file_info float-md-left show-file-upload">
                            <p className="mt-2 mb-1 text-uppercase show-file-name">
                              <b style={{ color: "#212529" }} alt={file.fileName}>{file.fileName}</b>
                            </p>
                            <p className="mb-1 text-uppercase">{Math.floor(file.origin.size / 1024)} KB</p>
                            <p className="mb-1">{getFileDate(file.origin.uploadTime)}</p>
                          </div>
                          <div className="file_change float-md-right text-center mt-3 mr-md-3">
                            <input disabled={isRequesting} type="file" hidden name="file" id="contract_file" {...getInputProps()} />
                            <label className="file_change_btn mb-0" href="javascript:void(0)" style={{ cursor: 'pointer' }}>
                              <span className="file_change_icon">
                                <IconOtherUpload />
                              </span>
                              <span className="mt-1 mb-2 mb-md-0">Tải lên bản khác</span>
                            </label>
                          </div>
                          <div className="clearfix"></div>
                        </div>
                      </>
                    )}
                  </div>
                </>
              )}
            </form>
          </div>
          <div className="lefttop_corner"></div>
          <div className="leftbottom_corner"></div>
          <div className="righttop_corner"></div>
          <div className="rightbottom_corner"></div>
        </div>
      </div>
      {file.pdf && (
        <div className="new_contract_nav text-center py-3">
          <button className="btn btn_outline_site mx-2 mx-sm-3 d-none">QUAY LẠI</button>
          <button disabled={isRequesting} className="btn btn_site mx-2 mx-sm-3" onClick={handleSubmit(onSubmit)}>
            TIẾP TỤC
          </button>
        </div>
      )}
    </>
  );
};

export default ContractCreateFormFile;
