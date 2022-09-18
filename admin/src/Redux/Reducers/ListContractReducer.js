import { LIST_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setListContracts(state, data) {
    return {
        ...state,
        ...data
    };
}

function setReadyForReloading(state, isReadyForReloading) {
    return {
        ...state,
        isReadyForReloading: isReadyForReloading
    }
}
function setIsCanceling(state, isCanceling) {
    return {
        ...state,
        isCanceling: isCanceling
    }
}
function setGlobalSigning(state, isGlobalSigning) {
    return {
        ...state,
        isGlobalSigning: isGlobalSigning
    }
}
const initialState = {
    contracts: null,
    currentPage: 0,
    totalPages: 1,
    pageSize: 10,
    totalItems: 0,
    isReadyForReloading: false,
    isCanceling: false,
    isGlobalSigning: false
};

function ListContractReducer(state = initialState, action) {
    switch (action.type) {
        case ACTION.SET_LIST_CONTRACTS:
            return setListContracts(state, action.payload);
        case ACTION.SET_READY_FOR_RELOADING:
            return setReadyForReloading(state, action.payload);
        case ACTION.SET_IS_CANCELING:
            return setIsCanceling(state, action.payload);
        case ACTION.SET_GLOBAL_SIGNING:
            return setGlobalSigning(state, action.payload);
        default:
            return state;
    }
}
export default ListContractReducer;
