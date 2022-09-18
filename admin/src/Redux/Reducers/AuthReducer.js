import { AUTH_ACTION } from "@Consts/action";

function setUser(state, action) {
  return {
    ...state,
    user: action.payload,
  }
}

function setLoggedOut(state, action) {
  return {
    ...state,
    user: null,
  }
}


function setLoadingProfile(state, action) {
  return {
    ...state,
    isLoadingProfile: action.payload
  }
}


const initialState = {
  isLoadingProfile: true,
  user: null
};

function AuthReducer(state = initialState, action) {
  switch (action.type) {
    case AUTH_ACTION.SET_USER:
      return setUser(state, action);
    case AUTH_ACTION.SET_LOADING_PROFILE: 
      return setLoadingProfile(state, action);
    case AUTH_ACTION.SET_LOGGED_OUT: 
      return setLoggedOut(state, action);
    default:
      return state;
  }
}

export default AuthReducer;