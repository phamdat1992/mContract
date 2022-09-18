package vn.amitgroup.digitalsignatureapi.service;

import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.entity.MarkAsRead;
import vn.amitgroup.digitalsignatureapi.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface MarkAsReadService {
    Boolean markRead(String token,Integer commentId);
    Boolean markReadByUser(String email,String contractId, Integer commentId);
    List<Notification> getMarkAsBySigner(String token,Boolean isRead);
    List<Notification> getMarkAsByUser(String email,String contractId,Boolean isRead);
    Boolean markReadNotificationBySigner(String token, UUID notificationId);
    Boolean markReadNotificationByUser(String email,String contractId, UUID notificationId);
    Boolean markAllNotificationByUser(String email,String type);
    BaseResponse<List<Notification>> getAllNotification(String email, String type, Integer currentPage, Integer size);
    MarkAsRead getMarkAsReadByContractId(String contractId);
    BaseResponse<List<MarkAsRead>> countAllNotification(String email);
}
