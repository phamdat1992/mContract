import { NEW_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setNewContracts(contracts) {
    return {
        type: ACTION.SET_NEW_CONTRACTS,
        payload: contracts,
    };
}

function setReadyForReloading(readyForReloading) {
    return {
        type: ACTION.SET_READY_FOR_RELOADING,
        payload: readyForReloading
    }
}
export {
    setNewContracts,
    setReadyForReloading
};