package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.amitgroup.digitalsignatureapi.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
   List<Comment> findByContract_IdOrderByCreatedTimeAsc(String contractId);
   List<Comment> findByContract_Id(String id);
   List<Comment> findByParentIdIsNullAndContract_Id(String id);
   List<Comment> findByParentId(Integer id);
}
