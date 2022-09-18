import { CREATE_CONTRACT_ACTION } from '@Consts/action';

function setStep(state, step) {
  return {
    ...state,
    ...{ currentStep: step },
  };
}

function setFile(state, data) {
  return {
    ...state,
    ...{ file: data },
  };
}

function setSigners(state, signers) {
  return {
    ...state,
    ...{ signers: signers },
  };
}

function updateSigner(state, signer) {
  const newSigners = Object.assign([], state.signers);
  newSigners.forEach((item, index) => {
    if (signer.index == item.index) {
      newSigners[index] = {
        ...item,
        ...signer,
      };
    }
  });
  return {
    ...state,
    ...{ signers: newSigners },
  };
}

function updateSignerPosition(state, data) {
  const newSigners = Object.assign([], state.signers);
  newSigners.forEach((item, index) => {
    if (data.index == item.index) {
      newSigners[index] = {
        ...item,
        ...{ position: { ...item.position, ...data.position } },
      };
    }
  });

  return {
    ...state,
    ...{ signers: newSigners },
  };
}

function updateCreatorPosition(state, data) {
  const newCreator = {
    ...state.creator,
    ...{ position: { ...state.creator.position, ...data.position } },
  };
  return {
    ...state,
    ...{ creator: newCreator },
  };
}

function setPdfCurrentPage(state, currentPage) {
  return {
    ...state,
    ...{ pdf: { ...state.pdf, ...{ currentPage: currentPage } } },
  };
}

function setPdfTotalPages(state, totalPages) {
  return {
    ...state,
    ...{ pdf: { ...state.pdf, ...{ totalPages: totalPages } } },
  };
}


function setPreviewCurrentPage(state, currentPage) {
  return {
    ...state,
    ...{ preview: { ...state.preview, ...{ currentPage: currentPage } } },
  };
}

function setPreviewTotalPages(state, totalPages) {
  return {
    ...state,
    ...{ preview: { ...state.preview, ...{ totalPages: totalPages } } },
  };
}

function setCreatingContract(state, isCreatingContract) {
  return {
    ...state,
    ...{ isCreatingContract: isCreatingContract },
  };
}

function setReadyForCreating(state, isReadyForCreating) {
  return {
    ...state,
    ...{ isReadyForCreating: isReadyForCreating },
  };
}

function setInformation(state, information) {
  return {
    ...state,
    ...{ information: information },
  };
}

function resetCreateContract() {
  return initialState;
}

const initialState = {
  currentStep: 1,
  file: {},
  creator: null,
  signers: [
    {
      index: 1,
      fullName: '',
      email: '',
      taxCode: '',
      position: {
        x: 0,
        y: 0,
        percentX: 0,
        percentY: 0,
        page: null,
      },
    },
  ],
  preview: {
    currentPage: 1,
    totalPages: 1,
  },
  pdf: {
    currentPage: 1,
    totalPages: 1,
  },
  information: {
    title: '',
    numberOFExpirationDate: '',
    content: '',
  },
  isCreatingContract: false,
  isReadyForCreating: false,
};

function CreateContractReducer(state = initialState, action) {
  switch (action.type) {
    case CREATE_CONTRACT_ACTION.SET_STEP:
      return setStep(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_FILE:
      return setFile(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_SIGNERS:
      return setSigners(state, action.payload);
    case CREATE_CONTRACT_ACTION.UPDATE_SIGNER:
      return updateSigner(state, action.payload);
    case CREATE_CONTRACT_ACTION.UPDATE_SIGNER_POSITION:
      return updateSignerPosition(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_PREVIEW_CURRENT_PAGE:
      return setPreviewCurrentPage(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_PREVIEW_TOTAL_PAGES:
      return setPreviewTotalPages(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_PDF_CURRENT_PAGE:
      return setPdfCurrentPage(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_PDF_TOTAL_PAGES:
      return setPdfTotalPages(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_CREATING_CONTRACT:
      return setCreatingContract(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_READY_FOR_CREATING:
      return setReadyForCreating(state, action.payload);
    case CREATE_CONTRACT_ACTION.SET_INFORMATION:
      return setInformation(state, action.payload);
    case CREATE_CONTRACT_ACTION.RESET_DATA:
      return resetCreateContract();
    default:
      return state;
  }
}

export default CreateContractReducer;
