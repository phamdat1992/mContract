import { configureStore } from "@reduxjs/toolkit";
import { ExampleReducer } from "../reducers";

const store = configureStore({
   reducer: {
      example: ExampleReducer
   }
});
export { store }