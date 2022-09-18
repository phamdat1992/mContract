import { Modal } from 'react-bootstrap';
import AvatarEditor from 'react-avatar-editor';
import { useState } from 'react';
import { BsPlus, BsDash } from 'react-icons/bs';
import React, { useRef } from 'react';
const UploadAvatarModal = ({ show, onHide, image, onSubmit, title, uploading = false }) => {
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
      // const canvasScaled = editorRef.current.getImageScaledToCanvas();
    }
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton className="py-2">
        <Modal.Title>{title ? title : 'Cập nhật logo công ty'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p className="text-center">Bạn có thể di chuyển hoặc zoom ảnh</p>
        <div className="upload-contain">
          <AvatarEditor
            ref={editorRef}
            image={image}
            width={250}
            height={250}
            borderRadius={125}
            scale={scale}
            border={1}
          // backgroundColor={[255, 255, 255, 1]}
          />
          <div className="d-flex align-items-center py-3">
            <BsDash className="mr-1" />
            <input name="scale" type="range" onChange={handleScale} min={allowZoomOut ? '0.1' : '1'} max="2" step="0.01" defaultValue="1" className="range-scale" />
            <BsPlus className="ml-1" color="#000" />
          </div>
        </div>
        <div className="text-right">
          {/* <button type="button" className="btn btn-secondary px-3 btn-sm" onClick={onHide}>Đóng</button> */}
          <button type="button" className="btn btn-primary px-3 btn-sm" onClick={onClickSave}>
            Xong
          </button>
        </div>
      </Modal.Body>
    </Modal>
  );
};
export default UploadAvatarModal;
