import { IconPlus } from "@Components/Icon";
import { Modal } from "react-bootstrap";
import { useState } from "react";
import { ContractCreateForm } from "@Containers/Contract";
import SimpleBar from 'simplebar-react';
import { useDispatch, useSelector } from "react-redux";
import { resetCreateContract } from "@Redux/Actions/CreateContract";

export default function ContractCreateButton() {
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);

  const onCloseModal = (isReset = true) => {
    if (isReset) {
      dispatch(resetCreateContract());
    }
    setShowModal(false);
  }

  return (
    <>
      <button className="btn btn_new" onClick={() => setShowModal(true)}>
        <IconPlus />
        <span>Tạo hợp đồng</span>
      </button>
      <Modal show={showModal} onHide={() => onCloseModal()} className="create-contract" dialogClassName="modal_full m-0 my-sm-0 mx-sm-auto">
        <Modal.Body className="p-0">
          <ContractCreateForm onCloseModal={() => onCloseModal()} />
        </Modal.Body>
      </Modal>
    </>
  );
}
