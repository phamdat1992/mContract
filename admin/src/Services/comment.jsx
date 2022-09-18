import { DefaultAPI, SignerAPI } from '@Utils/axios';
import { COMMENT } from '@Enums/endpoints';

class CommentService {
  static list = contractId => {
    const url = COMMENT.LIST.replace('{contractId}', contractId);
    return DefaultAPI.get(url);
  };

  static create = data => {
    const url = COMMENT.CREATE;
    return DefaultAPI.post(url, data);
  };

  static listCommentSigner = token => {
    const url = COMMENT.LIST_COMMENT_SIGNER;
    return SignerAPI.get(url, {
      headers: {
        Signer: `Bearer ${token}`,
      },
    });
  };

  static createCommentSigner = (token, data) => {
    const url = COMMENT.CREATE_COMMENT_SIGNER;
    return DefaultAPI.post(url, data, {
      headers: {
        Signer: `Bearer ${token}`,
      },
    });
  };

  static markCommentUser = (commentId) => {
    const url = COMMENT.MARKED_COMMENT_USER.replace('{commentId}', commentId);
    return DefaultAPI.put(url);
  };

  static markCommentSigner = (token, commentId) => {
    const url = COMMENT.MARKED_COMMENT_SIGNER.replace('{commentId}', commentId);
    return SignerAPI.put(url, {}, {
      headers: {
        Signer: `Bearer ${token}`,
      },
    });
  };
}
export default CommentService;
