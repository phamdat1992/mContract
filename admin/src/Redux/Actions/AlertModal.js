import { ALERT_MODAL_ACTION as ACTION } from "@Consts/action";

function setModal(status) {
    return {
        type: ACTION.SET_MODAL,
        payload: status,
    };
}

export { setModal };
