import { Modal } from 'react-bootstrap';
import AvatarEditor from 'react-avatar-editor';
import { useState } from 'react';
import { BsPlus, BsDash } from 'react-icons/bs';
import React, { useRef } from 'react';
const UploadLogoModal = ({ show, onHide, image, onSubmit, isLogo = false, uploading = false }) => {
  const [scale, setScale] = useState(1);
  const [allowZoomOut, setAllowZoomOut] = useState(true);
  let editorRef = useRef();
  const handleScale = e => {
    const scale = parseFloat(e.target.value);
    setScale(scale);
  };
  const onClickSave = () => {
    if (editorRef && !uploading) {
      const canvas = editorRef.current.getImage();
      const arrName = image.name.split('.');
      canvas.toBlob(blob => {
        let file = new File([blob], `${image.name}`, { type: `image/${arrName[arrName.length - 1]}` });
        onHide();
        onSubmit(file);
      }, `image/${arrName[arrName.length - 1]}`);
    }
  };

  return (
    <Modal show={show} onHide={onHide} contentClassName="modal_confirm modal_site">
      <Modal.Header className="bg_site">
        <h5 class="modal-title" id="exampleModalLabel_img_user">
          <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-file-image" viewBox="0 0 16 16">
            <path d="M8.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z" />
            <path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zM3 2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v8l-2.083-2.083a.5.5 0 0 0-.76.063L8 11 5.835 9.7a.5.5 0 0 0-.611.076L3 12V2z" />
          </svg>
        </h5>
        <button onClick={onHide} type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <div class="header_arrow border_top_site"></div>
      </Modal.Header>
      <Modal.Body>
        <div class="text-center">
          <h3 class="head_title">{isLogo ? 'Chỉnh sửa logo công ty' : 'Chỉnh sửa ảnh đại diện'}</h3>
        </div>
        <div class="box_img">
          <div className="upload-contain">
            <div className="img-editor">
              <AvatarEditor
                ref={editorRef}
                image={image}
                width={164}
                height={164}
                borderRadius={100}
                scale={scale}
                color={[175, 175, 174, 1]}
                border={1}
                backgroundColor="#fff"
                className="bg-white"
              />
            </div>
            <div className="d-flex align-items-center py-3">
              <BsDash className="mr-3" />
              <input name="scale" type="range" onChange={handleScale} min={allowZoomOut ? '0.1' : '1'} max="2" step="0.01" defaultValue="1" className="range-scale" />
              <BsPlus className="ml-3" color="#000" />
            </div>
          </div>
        </div>
        <div class="text-center">
          <button onClick={onHide} type="button" class="btn btn-secondary mx-auto">Hủy bỏ</button>
          <button onClick={onClickSave} type="button" class="btn btn-primary btn_pri1 mx-auto">Lưu ảnh</button>
        </div>
      </Modal.Body>
    </Modal>
  );
};
export default UploadLogoModal;