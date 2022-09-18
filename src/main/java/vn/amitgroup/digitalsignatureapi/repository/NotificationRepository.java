package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import vn.amitgroup.digitalsignatureapi.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    @Query("select n from Notification n where n.contractId= :contractId")
    List<Notification> findAllByContract(String contractId);
    @Query("select n from Notification n where n.userId= :userId")
    List<Notification> getNotificationByUser(Integer userId);
    @Query("select n from Notification n where n.id= :id")
    Notification getById(UUID id);
    @Query("select n from Notification n where n.contractId= :contractId and n.type='NEWCONTRACT'")
    List<Notification> getByContractId(String contractId);
}
