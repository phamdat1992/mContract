import { EXAMPLE_ACTION } from "../enums";

function start(state: any) {
   return {
      ...state,
      status: "STARTED",
   };
}
function end(state: any) {
   return {
      ...state,
      status: "ENDED",
   };
}
const initialState = {
   status: "ENDED",
};

function ExampleReducer(state = initialState, action: any) {
   switch (action.type) {
      case EXAMPLE_ACTION.START:
         return start(state);
      case EXAMPLE_ACTION.END:
         return end(state);
      default:
         return state;
   }
}
export default ExampleReducer;
