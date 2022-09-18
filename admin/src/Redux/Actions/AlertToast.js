import { ALERT_TOAST_ACTION as ACTION } from "@Consts/action";

function addToast(data) {
    
    return {
        type: ACTION.ADD_TOAST,
        payload: data,
    };
}
function removeToast(data) {
    return {
        type: ACTION.REMOVE_TOAST,
        payload: data
    }
}

export { addToast, removeToast };
