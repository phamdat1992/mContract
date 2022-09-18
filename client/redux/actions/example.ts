import { EXAMPLE_ACTION  } from "../enums"; 

function start() {
   return {
      type: EXAMPLE_ACTION.START
   }
}
function end() {
   return {
      type: EXAMPLE_ACTION.END
   }
}

export {start, end}