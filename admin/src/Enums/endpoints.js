const AUTH = {
   OTP_REGISTER: "/otps/register",
   OTP_LOGIN: "/otps/login",
   LOGIN: "/authentication/login",
   LOGOUT: "/authentication/logout",
   REGISTER: "/authentication/register",
   GET_USER: "/users/profile",
   UPDATE_USER: "/users",
   TOKEN: "/user/token",
   GET_NOTIFICATION_TYPE: '/users/notify',
   MARKED_ALL_NOTIFICATIONS: "/users/notification",
   MARKED_NOTIFICATION: "/users/notification/{contractId}/{notificationId}",
   ACCEPT_EMAIL_NOTI: "/users/notification",
   REFRESH_TOKEN: "/authentication/refreshtoken",
   GET_UNREAD_NOTIFICATION: '/users/notificationNumbers'
};

const TAGS = {
   LIST_CONTRACT_TAG: "/tags​/contract​/{contractId}",
   LIST_TAG: "/tags/counts",
   DELETE_TAG: "​/tag​s/{id}",
   ADD_TAG: "/tags",
   EDIT_TAG: "/tags",
   DETAIL_TAG: "/tags/{id}",
   COUNT_TAG: "/tags/counts"
};
const CONTRACT = {
   LIST_CONTRACT: "/contracts",
   ADD_CONTRACT: "/contracts",
   DELETE_CONTRACT: "/contracts/delete",
   STATIS_CONTRACT: "/contracts/statistical-datas",
   DETAIL_CONTRACT: "/contracts/{id}",
   DETAIL_CONTRACT_USER: "/contracts/{contractId}/user",
   DETAIL_CONTRACT_SIGNER: "/contracts/signer",
   UPDATE_WATCHED_CONTRACT: "/contracts/watched",
   ATTACH_TAG: "/contracts/tags",
   REMOVE_TAG: "/contracts/tags",
   CANCEL_CONTRACT: "/contracts/{contractId}/cancel",
   CANCEL_CONTRACT_SIGNER: "/contracts/signer/cancel",
   GET_SIGNER: "/signers",
   SIGN_CONTRACT: "/contracts/sign",
   SIGN_CONTRACT_SIGNER: "/contracts/sign/signer",
   UPLOAD_FILE: '/contracts/file',
   DATA_TO_SIGN: '/contracts/data-to-sign',
   DATA_TO_SIGN_SIGNER: '/contracts/data-to-sign/signer',
   CANCEL_CONTRACTS: '/contracts/cancel',
   UPDATE_AUTHOR_SIGNER: '/contracts/signer/invalid-taxcode',
   UPDATE_AUTHOR_USER: '/contracts/{contractId}/invalid-taxcode'
};
const FILE = {
   CONVERT_FILE: "/file-uploads/file-convert",
   DOWLOAD_FILE: "/file-uploads/file-s3s",

   UPLOAD_FILE: "/file-uploads",
   GET_FILE: "/s3-files/{id}",
   LIST_FILE: "/s3-files​",
}
const QUESTION = "/question/send-question";
const COMPANY = {
   UPDATE: "/companies",
   ADD: "/companies",
   GET: "/companies",
};
const COMMON = {
   PROVINCE_LIST: "/common/provinces",
   PROVINCE_DETAIL: "/common/provinces/{id}",
   DISTRICT_LIST: "/common​/districts",
   PROVINCE_DISTRICT: "/common/provinces/{id}/districts",
   DISTRICT_DETAIL: "/common​/districts​/{id}",
   WARD_LIST: "/common​/wards",
   DISTRICT_WARD: "/common/districts/{id}/wards",
   WARD_DETAIL: "/common​/wards​/{id}",
}
const COMMENT = {
   LIST: "/comments/contract/{contractId}",
   CREATE: "/comments",

   LIST_COMMENT_SIGNER: "/comments/signer",
   CREATE_COMMENT_SIGNER: "/comments/signer",

   MARKED_COMMENT_USER: "/comments/mark/{commentId}",
   MARKED_COMMENT_SIGNER: "/comments/signer/mark/{commentId}",
}
const NOTIFICATION_CONTRACT = {
   UNREAD_USER: "/users/notification/{contractId}",
   UNREAD_SIGNER: "/users/notification/signer",
   MARKED_NOTI_USER: '/users/notification/{contractId}/{notificationId}',
   MARKED_NOTI_SIGNER: '/users/notification/signer/{notificationId}',
}
export { AUTH, COMPANY, QUESTION, CONTRACT, TAGS, COMMON, COMMENT, FILE, NOTIFICATION_CONTRACT };
