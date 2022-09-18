import { ALERT_MODAL_ACTION } from "@Consts/action";
function setModal(state, data) {
    return {
        ...state, ...data
    }
}

const initialState = {
    isShow: false,
    title: "",
    message: ""
};

function AlertModalReducer(state = initialState, action) {
    switch (action.type) {
        case ALERT_MODAL_ACTION.SET_MODAL:
            return setModal(state, action.payload);
        default:
            return state;
    }
}
export default AlertModalReducer;