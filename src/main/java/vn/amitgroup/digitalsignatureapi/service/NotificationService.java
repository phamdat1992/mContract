package vn.amitgroup.digitalsignatureapi.service;

import vn.amitgroup.digitalsignatureapi.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<Notification> getAllNotificationByContract(List<String> contractId);
    void updateStatusWatched(Integer userId,String type);
    void updateWatched(UUID id);
}
