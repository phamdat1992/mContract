import { DefaultAPI } from "@Utils/axios";
import { AUTH, COMPANY } from "@Enums/endpoints";
import { set, get, remove } from '@Utils/cookie';
class AuthService {
   static login = (data) => {
      const url = AUTH.OTP_LOGIN;
      return Promise.resolve(DefaultAPI.post(url, data));
   };

   static isAuthenticated = () => {
      return !!get('digital_signature_token');
   }

   static saveToken = (data) => {
      set('digital_signature_token', data.token);
      set('digital_signature_refresh_token', data.refreshToken);
      set('digital_signature_token_type', data.type);
   }

   static logout = () => {
      const url = AUTH.LOGOUT;
      return Promise.resolve(DefaultAPI.get(url));
   };

   static signup = (data) => {
      const url = AUTH.OTP_REGISTER;
      return Promise.resolve(DefaultAPI.post(url, data));
   };

   static refreshToken = (data) => {
      const url = AUTH.REFRESH_TOKEN;
      return Promise.resolve(DefaultAPI.post(url, data));
   }

   static updateProfile = (data) => {
      const url = AUTH.UPDATE_USER;
      return Promise.resolve(DefaultAPI.put(url, data));
   };

   static enterCodeLogin = (data) => {
      const url = AUTH.LOGIN;
      return Promise.resolve(DefaultAPI.post(url, data));
   };

   static enterCodeRegister = (data) => {
      const url = AUTH.REGISTER;
      return Promise.resolve(DefaultAPI.post(url, data));
   };

   static acceptEmailNoti = (data) => {
      const url = AUTH.ACCEPT_EMAIL_NOTI;
      return Promise.resolve(DefaultAPI.put(url, data));
   };
   static profile = () => {
      const url = AUTH.GET_USER;
      return Promise.resolve(DefaultAPI.get(url));
   };
   static addCompany = (data) => {
      const url = COMPANY.ADD;
      return Promise.resolve(DefaultAPI.post(url, data));
   }
   static getNotificationType = (params) => {
      const url = AUTH.GET_NOTIFICATION_TYPE;
      return DefaultAPI.get(url, { params });
   }
   static markedAllNotifications = (params) => {
      const url = AUTH.MARKED_ALL_NOTIFICATIONS;
      return Promise.resolve(DefaultAPI.post(url, params));
   }
   static markedNotification = (contractId, notificationId) => {
      const url = (AUTH.MARKED_NOTIFICATION.replace('{contractId}', contractId)).replace('{notificationId}', notificationId);
      return Promise.resolve(DefaultAPI.post(url));
   }
   static getUnreadNotification = (params) => {
      const url = AUTH.GET_UNREAD_NOTIFICATION;
      return DefaultAPI.get(url, { params });
   }
}
export default AuthService;
