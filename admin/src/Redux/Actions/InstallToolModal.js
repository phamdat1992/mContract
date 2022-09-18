import { INSTALL_TOOL_MODAL_ACTION as ACTION } from "@Consts/action";


function setInstallToolModal(status) {
    return {
        type: ACTION.SET_INSTALL_TOOL_MODAL,
        payload: status,
    };
}

export { setInstallToolModal };
