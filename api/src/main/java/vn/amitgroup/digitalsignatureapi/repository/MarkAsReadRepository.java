package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.amitgroup.digitalsignatureapi.entity.MarkAsRead;

import java.util.List;
import java.util.UUID;

public interface MarkAsReadRepository extends JpaRepository<MarkAsRead, Integer> {
    @Query(value = "select mar from MarkAsRead mar where mar.signerId= :signerId and mar.commentId= :commentId and mar.type = 'COMMENT'")
    MarkAsRead getMarkAsReadBySigner_Comment(Integer signerId,Integer commentId);
    @Query(value = "select mar from MarkAsRead mar where mar.signerId= :signerId and mar.type = :type")
    List<MarkAsRead> getAllBySignerId(Integer signerId,String type);
    @Query(value = "select mar from MarkAsRead mar where mar.signerId= :signerId and (mar.type= 'COMMENT' or mar.type='SIGN') and mar.isRead= :isRead and mar.contractId= :contractId")
    List<MarkAsRead> getMarkAsBySigner(Integer signerId,Boolean isRead,String contractId);
    @Query(value = "select mar from MarkAsRead mar where mar.signerId= :signerId")
    List<MarkAsRead> getMarkAsBySignerNotType(Integer signerId);
    @Query("select mar from MarkAsRead mar where mar.notificationId= :id and mar.signerId= :signerId")
    MarkAsRead getNotificationFromMarkAsRead(UUID id, Integer signerId);
    @Query("select mar from MarkAsRead mar where mar.email= :email and mar.type= :type")
    List<MarkAsRead> getMarkAsByEmail(String email,String type);
    @Query("select mar from MarkAsRead mar where mar.email= :email and (mar.type= 'COMMENT' or mar.type='SIGN')")
    List<MarkAsRead> getMarkAsAllByCmtAndSign(String email);
    @Query("select mar from MarkAsRead mar where mar.contractId= :contractId")
    List<MarkAsRead> getMarkAsReadByContractId(String contractId);
    @Query("select mar from MarkAsRead mar where mar.email= :email and is_read = 'false' and (type = 'EXPIRE' or type = 'COMMENT' or type ='SIGN' or type ='NEWCONTRACT')")
    List<MarkAsRead> countNotification(String email);
}
