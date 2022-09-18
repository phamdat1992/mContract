package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.amitgroup.digitalsignatureapi.entity.MarkAsReadComment;


public interface MarkAsReadCommentRepository extends JpaRepository<MarkAsReadComment, Integer> {
    @Query("select mar from MarkAsReadComment mar where mar.commentId= :commentId and mar.email= :email")
    MarkAsReadComment getMarkAsReadCommentByCommentId(Integer commentId,String email);
}
