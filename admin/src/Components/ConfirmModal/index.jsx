import { Modal } from "react-bootstrap";
const ConfirmModal = ({ show, contractsName, userName = '', onHide, onSubmit }) => {

    return (
        <Modal show={show} onHide={onHide} contentClassName="modal_confirm modal_site">
            <Modal.Header className="bg_site_cancel">
                <h5 class="modal-title" id="exampleModalLabel_cancel">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
                        <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z" />
                    </svg>
                </h5>
                <button onClick={onHide} type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div class="header_arrow border_top_site_cancel"></div>
            </Modal.Header>
            <Modal.Body>
                <div class="text-center">
                    <h3 class="head_title">Xác nhận hủy Hợp đồng</h3>
                </div>
                <p>Kính chào Quý khách hàng: <b>{userName}</b>.</p>
                <p>Để xác nhận hủy <b>“{contractsName}”</b>.
                    Quý khách vui lòng ấn nút hủy Hợp đồng.</p>
                <div class="text-center">
                    <button type="button" onClick={onSubmit} class="btn btn-primary btn_pri1 mx-auto">Hủy Hợp đồng</button>
                    <i class="d-block mt-3">Cảm ơn quý khách đã sử dụng dịch vụ <b class="text-site"><a href="https://mcontract.vn/" target="_blank">MContract</a></b>.</i>
                </div>
            </Modal.Body>
        </Modal>
    )
    // return <>
    //     <Modal show={show} onHide={onHide}>
    //         <Modal.Body>
    //             <p>{message}</p>
    //             <div className="text-right mt-3">
    //                 <button type="button" className="btn btn-primary px-3 btn-sm mr-2" onClick={onSubmit}>Có</button>
    //                 <button type="button" className="btn btn-outline-secondary px-3 btn-sm" onClick={onHide}>Hủy</button>
    //             </div>
    //         </Modal.Body>
    //     </Modal>
    // </>
}
export default ConfirmModal