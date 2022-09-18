import { CREATE_CONTRACT_ACTION } from '@Consts/action';

function setStep(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_STEP,
    payload: data,
  };
}

function setFile(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_FILE,
    payload: data,
  };
}

function setSigners(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_SIGNERS,
    payload: data,
  };
}

function setPreviewCurrentPage(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_PREVIEW_CURRENT_PAGE,
    payload: data,
  };
}

function setPreviewTotalPages(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_PREVIEW_TOTAL_PAGES,
    payload: data,
  };
}

function setPdfCurrentPage(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_PDF_CURRENT_PAGE,
    payload: data,
  };
}

function setPdfTotalPages(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_PDF_TOTAL_PAGES,
    payload: data,
  };
}

function updateSigner(signer) {
  return {
    type: CREATE_CONTRACT_ACTION.UPDATE_SIGNER,
    payload: signer,
  };
}

function updateSignerPosition(data) {
  return {
    type: CREATE_CONTRACT_ACTION.UPDATE_SIGNER_POSITION,
    payload: data,
  };
}


function setCreatingContract(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_CREATING_CONTRACT,
    payload: data,
  };
}

function setReadyForCreating(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_READY_FOR_CREATING,
    payload: data,
  };
}

function setInformation(data) {
  return {
    type: CREATE_CONTRACT_ACTION.SET_INFORMATION,
    payload: data,
  };
}

function resetCreateContract() {
  return {
    type: CREATE_CONTRACT_ACTION.RESET_DATA
  }
}

export { setStep, setFile, setSigners, setPreviewCurrentPage, setPreviewTotalPages, setPdfCurrentPage, setPdfTotalPages, updateSigner, updateSignerPosition, setCreatingContract, setReadyForCreating, setInformation, resetCreateContract };
