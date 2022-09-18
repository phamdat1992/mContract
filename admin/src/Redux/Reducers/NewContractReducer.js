import { NEW_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setNewContracts(state, contracts) {
    return {
        ...state,
        newContracts: contracts
    };
}

function setReadyForReloading(state, isReadyForReloading) {
    return {
        ...state,
        isReadyForReloading: isReadyForReloading
    }
}

const initialState = {
    newContracts: null,
    isReadyForReloading: false
};

function NewContractReducer(state = initialState, action) {
    switch (action.type) {
        case ACTION.SET_NEW_CONTRACTS:
            return setNewContracts(state, action.payload);
        case ACTION.SET_READY_FOR_RELOADING: 
            return setReadyForReloading(state, action.payload);
        default:
            return state;
    }
}
export default NewContractReducer;
