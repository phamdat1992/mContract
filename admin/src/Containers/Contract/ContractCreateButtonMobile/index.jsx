import { Modal } from 'react-bootstrap';
import { useState } from 'react';
import { ContractCreateForm } from '@Containers/Contract';
import { IconBigPlus } from '@Components/Icon';
import { useDispatch, useSelector } from "react-redux";
import { resetCreateContract } from "@Redux/Actions/CreateContract";
import SimpleBar from 'simplebar-react';
function ContractCreateButtonMobile(props) {
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);

  const onCloseModal = (isReset = true) => {
    if (isReset) {
      dispatch(resetCreateContract());
    }
    setShowModal(false);
  };

  return (
    <>
      <button className="btn btn_site mnc_btn d-block d-lg-none" onClick={() => setShowModal(true)}>
        <IconBigPlus />
      </button>
      <SimpleBar>
        <Modal show={showModal} onHide={() => onCloseModal()} className="create-contract" dialogClassName="modal_full m-0 my-sm-0 mx-sm-auto">
          <Modal.Body className="p-0">
            <ContractCreateForm onCloseModal={() => onCloseModal()} />
          </Modal.Body>
        </Modal>
      </SimpleBar>
    </>
  );
}

export default ContractCreateButtonMobile;
