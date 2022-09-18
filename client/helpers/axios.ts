import axios from "axios";
// https://mcontract.vn
// https://digital-signature.api.demo-amit.com
const BASE_URL = "https://mcontract.vn/api/";
const Axios = axios.create({
   baseURL: BASE_URL
});

Axios.interceptors.request.use(
   req => {
      return req;
   },
   error => {
      return error;
   }
);

Axios.interceptors.response.use(
   res => {
      return res;
   },
   err => {
      console.log(err)
      return err;
   }
);
export default Axios;