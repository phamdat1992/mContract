package vn.amitgroup.digitalsignatureapi.service;

import java.util.List;

import vn.amitgroup.digitalsignatureapi.dto.CommentDto;
import vn.amitgroup.digitalsignatureapi.dto.CommentForm;
import vn.amitgroup.digitalsignatureapi.dto.ParentComment;

public interface CommentService {
    CommentDto addComment(CommentForm commentForm);
    List<ParentComment> getComment(String contractId);
    CommentDto addCommentByLink(CommentForm commentForm,String token);
    List<ParentComment> getBySigner(String token);
    Boolean markAsReadComment(String email,Integer commentId);
//    List<ParentComment> getAllByContractId(Integer contractId);
}
