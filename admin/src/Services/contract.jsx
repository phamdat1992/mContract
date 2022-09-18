import { DefaultAPI, SignerAPI } from "@Utils/axios";
import { CONTRACT, NOTIFICATION_CONTRACT, FILE } from "@Enums/endpoints";
const BASE_URL = "https://mcontract.vn/api";
class ContractService {

   static getContract = (params) => {
      const url = CONTRACT.LIST_CONTRACT;
      return DefaultAPI.get(url, { params });
   }

   static getStatisContract = () => {
      const url = CONTRACT.STATIS_CONTRACT;
      return DefaultAPI.get(url);
   }

   static updateWatched = (data) => {
      const url = CONTRACT.UPDATE_WATCHED_CONTRACT;
      return DefaultAPI.put(url, data);
   }


   static uploadFile = (id, data) => {
      const url = `${CONTRACT.UPLOAD_FILE}?contractId=${id}`;
      return DefaultAPI.post(url, data);
   }

   static convertFile = (data) => {
      const url = FILE.CONVERT_FILE;
      return DefaultAPI.post(url, data);
   }

   static getFile = (id) => {
      const url = FILE.GET_FILE.replace('{id}', id);
      return DefaultAPI.get(url);
   }

   static addContract(data) {
      const url = CONTRACT.ADD_CONTRACT;
      return DefaultAPI.post(url, data);
   }

   static detailContract(id) {
      const url = CONTRACT.DETAIL_CONTRACT.replace('{id}', id);
      return DefaultAPI.get(url);
   }

   static detailContractUser(id) {
      const url = CONTRACT.DETAIL_CONTRACT_USER.replace('{contractId}', id);
      return DefaultAPI.get(url);
   }

   static detailContractSigner(token) {
      const url = CONTRACT.DETAIL_CONTRACT_SIGNER;
      return SignerAPI.get(url, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      })
   }

   static attachTag = (data) => {
      const url = CONTRACT.ATTACH_TAG;
      return DefaultAPI.post(url, data);
   }

   static removeTag = (data) => {
      const url = CONTRACT.REMOVE_TAG;
      return DefaultAPI.delete(url, { data });
   }

   static deleteContract = (data) => {
      const url = CONTRACT.DELETE_CONTRACT;
      return DefaultAPI.post(url, data);
   }

   static cancelContract = (contractId, params) => {
      const url = CONTRACT.CANCEL_CONTRACT.replace('{contractId}', contractId);
      return DefaultAPI.put(url, params);
   }
   static cancelContractSigner = (token) => {
      const url = CONTRACT.CANCEL_CONTRACT_SIGNER;
      return SignerAPI.put(url, {}, {
         headers: {
            "Signer": `Bearer ${token}`,
         }
      });
   }
   static cancelContracts = (params) => {
      const url = CONTRACT.CANCEL_CONTRACTS;
      return DefaultAPI.put(url, params);
   }

   static dowloadContract = (data) => {
      const url = FILE.DOWLOAD_FILE;
      return DefaultAPI.post(url, data);
   }

   static inforSigner(token) {
      const url = CONTRACT.GET_SIGNER;
      return SignerAPI.get(url, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      });
   }

   static signContract(data) {
      const url = CONTRACT.SIGN_CONTRACT;
      return DefaultAPI.post(url, data);
   }

   static signContractSigner(token, data) {
      const url = CONTRACT.SIGN_CONTRACT_SIGNER;
      return SignerAPI.post(url, data, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      })
   }

   static notificationsUnreadUser(contractId, params) {
      const url = NOTIFICATION_CONTRACT.UNREAD_USER.replace('{contractId}', contractId);
      return DefaultAPI.get(url, { params });
   }

   static notificationsUnreadSigner(token, params) {
      const url = NOTIFICATION_CONTRACT.UNREAD_SIGNER;
      return SignerAPI.get(url, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
         params: params
      });
   }

   static markNotiContractUser(contractId, notificationId) {
      const url = NOTIFICATION_CONTRACT.MARKED_NOTI_USER.replace('{contractId}', contractId).replace('{notificationId}', notificationId);
      return DefaultAPI.post(url);
   }

   static markNotiContractSigner(token, notificationId) {
      const url = NOTIFICATION_CONTRACT.MARKED_NOTI_SIGNER.replace('{notificationId}', notificationId);
      return SignerAPI.post(url, {}, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      });
   }

   static dataToSign(data) {
      const url = CONTRACT.DATA_TO_SIGN;
      return DefaultAPI.post(url, data);
   }

   static dataToSignSigner(token, data) {
      const url = CONTRACT.DATA_TO_SIGN_SIGNER;
      return SignerAPI.post(url, data, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      })
   }

   static updateAuthorSigner(token) {
      const url = CONTRACT.UPDATE_AUTHOR_SIGNER;
      return SignerAPI.put(url, {}, {
         headers: {
            "Signer": `Bearer ${token}`,
         },
      });
   }

   static updateAuthorUser(contractId) {
      const url = CONTRACT.UPDATE_AUTHOR_USER.replace('{contractId}', contractId);
      return DefaultAPI.put(url, {});
   }
}

export default ContractService;
