import { Button, Modal } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { setModal } from '@Redux/Actions/AlertModal';
const AlertModal = (props) => {
    const { isShow, title, message } = useSelector(state => state.alertModal);
    const dispatch = useDispatch();

    const handleClose = () => dispatch(setModal({ isShow: false }));
    return <>
        <Modal show={isShow} onHide={handleClose}>
            {title && <Modal.Header className="py-2" closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>}
            <Modal.Body>
                <p>{message}</p>
                <div className="text-right mt-3">
                    <button type="button" className="btn btn-secondary px-3 btn-sm" onClick={handleClose}>Đóng</button>
                </div>
            </Modal.Body>
        </Modal>
    </>
}
export default AlertModal