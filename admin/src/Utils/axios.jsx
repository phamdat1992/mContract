import * as Axios from 'axios';
import { get } from '@Utils/cookie';
import queryString from 'query-string';
import { remove } from './cookie';
import AuthService from '@Services/auth';
import RefreshToken from '@Utils/refresh-token';
// https://mcontract.vn
const API_BASE_URL = 'https://mcontract.vn/api';
const TOOL_BASE_URL = 'http://localhost:8089';

/**
 * 13/12/2021
 * Cập nhật lại cấu trúc error trả về khi có lỗi
 * Cấu trúc cũ: string,
 * Cấu trúc mới: { errorMsg: string, errorCode: string }
 * 
 */

const DefaultAPI = Axios.create({
  baseURL: API_BASE_URL,
  crossDomain: true,
  withCredentials: true,
  // headers: {
  //   'content-type': 'application/json',
  // },
  paramsSerializer: params => queryString.stringify(params),
});

DefaultAPI.interceptors.request.use(request => {
  const token = get('digital_signature_token');
  const tokenType = get('digital_signature_token_type');
  if (tokenType && token) {
    request.headers.Authorization = `${tokenType} ${token}`;
  }
  return request;
});

DefaultAPI.interceptors.response.use(
  response => {
    if (response.data && [200, 201, 202, 203, 204].indexOf(response.data.statusCode) === -1) {
      return Promise.reject(response.data.errors);
    }
    return response.data;
  },
  async error => {
    let errorMsg = '';
    let errorCode = '';
    const originalRequest = error.config;
    if (error.response && [401].indexOf(error.response.status) !== -1) {
      try {
        originalRequest._retry = true;
        const res = await RefreshToken.refreshToken();
        AuthService.saveToken(res.data);
        return DefaultAPI(originalRequest);
      } catch {
        remove('digital_signature_token');
        remove('digital_signature_refresh_token');
        remove('digital_signature_token_type');
        window.location.href = '/dang-nhap';
      }
    }
    if (error.response && error.response.data && error.response.data.message) {
      errorMsg = error.response.data.message;
    } else {
      errorMsg = error.message;
    }

    if (error.response && error.response.data && error.response.data.errorCode) {
      errorCode = error.response.data.errorCode;
    } else {
      errorCode = 'ERROR';
    }

    return Promise.reject({
      errorMsg: errorMsg,
      errorCode: errorCode,
    });
  },
);

const SignerAPI = Axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'content-type': 'application/json',
  },
  paramsSerializer: params => queryString.stringify(params),
});

SignerAPI.interceptors.response.use(
  response => {
    if (response.data && [200, 201, 202, 203, 204].indexOf(response.data.statusCode) === -1) {
      return Promise.reject(response.data.errors);
    }
    return response.data;
  },
  error => {
    let errorMsg = '';
    let errorCode = '';
    if (error.response && error.response.data && error.response.data.message) {
      errorMsg = error.response.data.message;
    } else {
      errorMsg = error.message;
    }

    if (error.response && error.response.data && error.response.data.errorCode) {
      errorCode = error.response.data.errorCode;
    } else {
      errorCode = 'ERROR';
    }

    return Promise.reject({
      errorMsg: errorMsg,
      errorCode: errorCode,
    });
  },
);

const ToolAPI = Axios.create({
  baseURL: TOOL_BASE_URL,
  headers: {
    'content-type': 'application/json',
  },
  paramsSerializer: params => queryString.stringify(params),
});

ToolAPI.interceptors.response.use(
  response => {
    if (response.data && [200, 201, 202, 203, 204].indexOf(response.data.statusCode) === -1) {
      return Promise.reject(response.data.errors);
    }
    return response.data;
  },
  error => {
    let message = '';
    if ([400].indexOf(error.response.status) !== -1 && error.response.data.errors) {
      return Promise.reject(error.response.data.errors);
    } else {
      let errorMsg = '';
      let errorCode = '';
      if (error.response && error.response.data && error.response.data.message) {
        errorMsg = error.response.data.message;
      } else {
        errorMsg = error.message;
      }

      if (error.response && error.response.data && error.response.data.errors) {
        errorCode = error.response.data.errors;
      } else {
        errorCode = 'ERROR';
      }

      return Promise.reject({
        errorMsg: errorMsg,
        errorCode: errorCode,
      });
    }

    // let message = '';
    // if (error.response && error.response.data && error.response.data.message) {
    //   message = error.response.data.message;
    // } else {
    //   message = error.message;
    // }
    // return Promise.reject(message);
  },
);

export { DefaultAPI, ToolAPI, SignerAPI };
