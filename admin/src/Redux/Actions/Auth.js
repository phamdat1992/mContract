import { AUTH_ACTION } from "@Consts/action";

function setUser(user) {
   return {
      type: AUTH_ACTION.SET_USER,
      payload: user,
   };
}
function setLoadingProfile(val) {
   return {
      type: AUTH_ACTION.SET_LOADING_PROFILE,
      payload: val,
   };
}

function setLoggedOut() {
   return {
      type: AUTH_ACTION.SET_LOGGED_OUT
   }
}

export { setUser, setLoadingProfile, setLoggedOut };
