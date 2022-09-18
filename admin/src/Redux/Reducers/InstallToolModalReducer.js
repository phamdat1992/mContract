import { INSTALL_TOOL_MODAL_ACTION as ACTION } from "@Consts/action";
function setInstallToolModal(state, data) {
    return {
        ...state, ...data
    }
}

const initialState = {
    isShow: false,
    userName: ''
};

function InstallToolModalReducer(state = initialState, action) {
    switch (action.type) {
        case ACTION.SET_INSTALL_TOOL_MODAL:
            return setInstallToolModal(state, action.payload);
        default:
            return state;
    }
}
export default InstallToolModalReducer;