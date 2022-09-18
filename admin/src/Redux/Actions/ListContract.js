import { LIST_CONTRACT_ACTION as ACTION } from '@Consts/action';

function setListContracts(data) {
  return {
    type: ACTION.SET_LIST_CONTRACTS,
    payload: data,
  };
}

function setReadyForReloading(readyForReloading) {
  return {
    type: ACTION.SET_READY_FOR_RELOADING,
    payload: readyForReloading,
  };
}
function setIsCanceling(isCanceling) {
  return {
    type: ACTION.SET_IS_CANCELING,
    payload: isCanceling,
  };
}
function setGlobalSigning(isGlobalSigning) {
  return {
    type: ACTION.SET_GLOBAL_SIGNING,
    payload: isGlobalSigning,
  };
}
export { setListContracts, setReadyForReloading, setIsCanceling, setGlobalSigning };
