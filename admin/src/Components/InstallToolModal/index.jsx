import { Button, Modal } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { setInstallToolModal } from '@Redux/Actions/InstallToolModal';
const InstallToolModal = () => {
    const user = useSelector(state => state.auth.user);
    const { isShow, userName } = useSelector(state => state.installToolModal);
    const { contract, currentSignerToken } = useSelector(state => state.detailContract);
    const dispatch = useDispatch();
    const handleClose = () => dispatch(setInstallToolModal({ isShow: false }));
    return <>
        <Modal show={isShow} onHide={handleClose} contentClassName="modal_confirm modal_site">
            <Modal.Header className="bg_site">
                <h5 className="modal-title" id="exampleModalLabel_partner">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" className="bi bi-gear" viewBox="0 0 16 16">
                        <path d="M8 4.754a3.246 3.246 0 1 0 0 6.492 3.246 3.246 0 0 0 0-6.492zM5.754 8a2.246 2.246 0 1 1 4.492 0 2.246 2.246 0 0 1-4.492 0z" />
                        <path d="M9.796 1.343c-.527-1.79-3.065-1.79-3.592 0l-.094.319a.873.873 0 0 1-1.255.52l-.292-.16c-1.64-.892-3.433.902-2.54 2.541l.159.292a.873.873 0 0 1-.52 1.255l-.319.094c-1.79.527-1.79 3.065 0 3.592l.319.094a.873.873 0 0 1 .52 1.255l-.16.292c-.892 1.64.901 3.434 2.541 2.54l.292-.159a.873.873 0 0 1 1.255.52l.094.319c.527 1.79 3.065 1.79 3.592 0l.094-.319a.873.873 0 0 1 1.255-.52l.292.16c1.64.893 3.434-.902 2.54-2.541l-.159-.292a.873.873 0 0 1 .52-1.255l.319-.094c1.79-.527 1.79-3.065 0-3.592l-.319-.094a.873.873 0 0 1-.52-1.255l.16-.292c.893-1.64-.902-3.433-2.541-2.54l-.292.159a.873.873 0 0 1-1.255-.52l-.094-.319zm-2.633.283c.246-.835 1.428-.835 1.674 0l.094.319a1.873 1.873 0 0 0 2.693 1.115l.291-.16c.764-.415 1.6.42 1.184 1.185l-.159.292a1.873 1.873 0 0 0 1.116 2.692l.318.094c.835.246.835 1.428 0 1.674l-.319.094a1.873 1.873 0 0 0-1.115 2.693l.16.291c.415.764-.42 1.6-1.185 1.184l-.291-.159a1.873 1.873 0 0 0-2.693 1.116l-.094.318c-.246.835-1.428.835-1.674 0l-.094-.319a1.873 1.873 0 0 0-2.692-1.115l-.292.16c-.764.415-1.6-.42-1.184-1.185l.159-.291A1.873 1.873 0 0 0 1.945 8.93l-.319-.094c-.835-.246-.835-1.428 0-1.674l.319-.094A1.873 1.873 0 0 0 3.06 4.377l-.16-.292c-.415-.764.42-1.6 1.185-1.184l.292.159a1.873 1.873 0 0 0 2.692-1.115l.094-.319z" />
                    </svg>
                </h5>
                <button type="button" onClick={handleClose} className="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div className="header_arrow border_top_site"></div>
            </Modal.Header>
            <Modal.Body>
                <div className="text-center">
                    <h3 className="head_title">Cài đặt tool ký số</h3>
                </div>
                <p>Kính chào Quý khách hàng{userName ? <>: <b>{userName}</b>.</> : '.'}</p>
                {
                    ((user && user.fullName) || currentSignerToken) ? <>
                        <p>Quý khách vui lòng <b>cài đặt tool ký số</b>  và <b>cắm USB Token</b> để hoàn tất quá trình ký số.</p>
                    </> :
                        <>
                            <p>Quý khách vui lòng <b>cài đặt tool ký số</b>  và <b>cắm USB Token</b>  để hoàn tất đăng ký toàn khoản <b><a href="https://mcontract.vn" target="_blank">MContract</a></b>.</p>
                        </>
                }
                <div className="text-center">
                    <a type="button" className="btn btn-primary btn_pri1 mx-auto a-button a-download" href="https://mcontract.vn/uploads/MContract.zip" download>Tải tool ký số</a>
                    {user ?
                        <>
                            <i class="d-block mt-3">Cảm ơn quý khách đã sử dụng dịch vụ <b class="text-site"><a href="https://mcontract.vn" target="_blank">MContract</a></b>.</i>
                        </> :
                        <>
                            <i class="d-block mt-3">Đăng ký tài khoản <b class="text-site"><a href="https://mcontract.vn" target="_blank">MContract</a></b> hoàn toàn miễn phí <b><a href="https://mcontract.vn/dang-ki" target="_blank">tại đây</a></b>.</i>
                        </>
                    }
                </div>
            </Modal.Body>
        </Modal>
    </>
}
export default InstallToolModal