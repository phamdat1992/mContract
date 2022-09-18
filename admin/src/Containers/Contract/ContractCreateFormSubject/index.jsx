import { useForm } from 'react-hook-form';
import { Spinner } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { setInformation, setReadyForCreating, setStep } from '@Redux/Actions/CreateContract';
import { useState } from 'react';
const ContractCreateFormSubject = ({ onCloseModal }) => {
  const [counter, setCounter] = useState(512);
  const user = useSelector(state => state.auth.user);
  const { file, signers, information, currentStep, isCreatingContract } = useSelector(state => state.createContract);
  const dispatch = useDispatch();
  const {
    handleSubmit,
    getValues,
    register,
    setValue,
    formState: { errors, isValid },
  } = useForm({
    defaultValues: information,
  });
  const preventText = event => {
    if (event.target.innerText.length === 512 && event.keyCode != 8) {
      event.preventDefault();
    }
  }
  const pasteContent = event => {
    event.preventDefault();
    var text = event.clipboardData.getData("text/plain");
    document.execCommand("insertHTML", false, text);
  }
  const counterLetter = e => {
    // const contentContract = document.getElementById('step4_content');
    const textArea = e.target;
    if (textArea.scrollHeight > window.innerHeight - 590) {
      textArea.style.height = 'auto';
      textArea.style.height = textArea.scrollHeight + 'px';
    } else {
      textArea.style.height = 'calc(100vh - 590px)';
    }
    if (textArea.innerText.length > 0) {
      setCounter(512 - textArea.innerText.length);
      setValue('content', textArea.innerText);
    } else {
      setValue('content', '');
      setCounter(512);
    }
  };
  const hasAllSpace = () => {
    return getValues('content').trim().length > 0;
  }
  const onSubmit = async values => {
    dispatch(setInformation(values));
    dispatch(setReadyForCreating(true));
    onCloseModal(false);
  };

  return (
    <>
      <div className="new_contract_body">
        <div className="ncbody_wrap m-3 my-4 m-md-5 text-center has_ncnav">
          <div className="top_border"></div>
          <div className="nc_body_content">
            <form onSubmit={handleSubmit(onSubmit)}>
              <div className="container-fluid sending py-4 px-3 px-md-4">
                <div className="form-inline">
                  <div className="col-12 col-md-8 col-lg-9 mr-md-3 mb-3 px-0">
                    <input type="text" autoComplete="off" className={`form-control w-100 ${errors.title && 'is-invalid'}`} name="title" placeholder="Tiêu đề" title="Tiêu đề" {...register('title', { required: 'Xin mời nhập tiêu đề', pattern: {
                      value: /^([\s]{0,})([^\s.]+\s?){1,}([\s]{0,})$/g,
                      message: 'Mời nhập tiêu đề'
                    } })} />
                    {/* <div className="text-danger text-left mb-1" style={{height: '20px', fontSize: '14px'}}>{errors.title && errors.title.message}</div> */}
                  </div>
                  <div className="col mb-3 px-0">
                    <select className={`custom-select w-100 ${errors.numberOFExpirationDate && 'is-invalid'}`} title="Thời hạn ký" name="numberOFExpirationDate" {...register('numberOFExpirationDate', { required: 'Xin mời chọn thời gian ký' })}>
                      <option value="" disabled hidden>
                        Thời hạn ký
                      </option>
                      <option value="1">1 ngày</option>
                      <option value="7">1 tuần</option>
                      <option value="30">1 tháng</option>
                      <option value="90">3 tháng</option>
                      <option value="18250">Vô thời hạn</option>
                    </select>
                    {/* <div className="text-danger text-left mb-1" style={{height: '20px', fontSize: '14px'}}>{errors.title && errors.numberOFExpirationDate.message}</div> */}
                  </div>
                </div>
                <div className="wrap-typing">
                  <div onPaste={e => pasteContent(e)} onKeyDown={e => preventText(e)} id="step4_content" contentEditable="true" placeholder="Nội dung tin nhắn" title="Nội dung tin nhắn" className={`form-control editable-step4 overflow-hidden ${errors.content ? 'is-invalid' : ''}`} onInput={e => counterLetter(e)} {...register('content', { required: 'Xin mời nhập nội dung tin nhắn', maxLength: 512, validate: hasAllSpace })}></div>
                  {counter < 512 && (
                    <span id="count" className="counter">
                      ({counter})
                    </span>
                  )}
                  {/* <textarea id="step4_content" onKeyUp={counterLetter} maxLength="512" className={`form-control overflow-hidden ${errors.content && 'is-invalid'}`} placeholder="Nội dung tin nhắn" title="Nội dung tin nhắn" name="content" {...register('content', { required: 'Xin mời nhập nội dung tin nhắn' })}></textarea> */}
                  {/* {errors.content && <div className="text-danger text-left">{errors.content.message}</div>} */}
                </div>
                <hr />
                <p className="text-left mb-2 show-ellipsis">
                  Hợp đồng: <span className="text-site">{file.origin.name}</span>
                </p>
                <hr style={{ marginTop: '12px' }} />
                <p className="text-left mb-0">
                  Người nhận:{' '}
                  {signers.map((signer, index) => (
                    <>
                      {signer.email != user.email && (
                        <>
                          <b>{signer.fullName} </b>
                          <span className="text-secondary">
                            ({signer.email}){`${index < signers.length - 1 ? ', ' : ''}`}
                          </span>
                        </>
                      )}
                    </>
                  ))}
                </p>
              </div>
            </form>
          </div>
          <div className="lefttop_corner"></div>
          <div className="leftbottom_corner"></div>
          <div className="righttop_corner"></div>
          <div className="rightbottom_corner"></div>
        </div>
      </div>

      <div className="new_contract_nav text-center py-3">
        <button className="btn btn_outline_site mx-2 mx-sm-3" onClick={() => dispatch(setStep(currentStep - 1))}>
          QUAY LẠI
        </button>
        <button className="btn btn_site mx-2 mx-sm-3" onClick={handleSubmit(onSubmit)}>
          {isCreatingContract ? (
            <>
              <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....
            </>
          ) : (
            <>GỬI ĐI</>
          )}
        </button>
      </div>
    </>
  );
};

export default ContractCreateFormSubject;
