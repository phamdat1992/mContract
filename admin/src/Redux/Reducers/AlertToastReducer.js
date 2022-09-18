import { ALERT_TOAST_ACTION as ACTION } from "@Consts/action";

function addToast(state, data) {
    return {
        ...state, ...{ toasts: [...state.toasts, ...[data]] }
    }
}

function removeToast(state, data) {
    return {
        ...state,
        ...{ toasts: Object.assign([], state.toasts).filter(item => item.id != data.id) },
    };
}

const initialState = {
    toasts: []
};

function AlertToastReducer(state = initialState, action) {
    switch (action.type) {
        case ACTION.ADD_TOAST:
            return addToast(state, action.payload);
        case ACTION.REMOVE_TOAST:
            return removeToast(state, action.payload);
        default:
            return state;
    }
}
export default AlertToastReducer;