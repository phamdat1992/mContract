package vn.amitgroup.digitalsignatureapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.entity.Notification;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.repository.NotificationRepository;
import vn.amitgroup.digitalsignatureapi.service.NotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;


    @Override
    public List<Notification> getAllNotificationByContract(List<String> contractId) throws ApiException {
        List<Notification> list = new ArrayList<>();
        for (String contractid : contractId) {
            List<Notification> listChild = notificationRepository.findAllByContract(contractid);
            list.addAll(listChild);
        }
        return list;
    }

    @Override
    public void updateStatusWatched(Integer userId, String type) {
        List<Notification> listNotify = notificationRepository.getNotificationByUser(userId);
        for (Notification notification : listNotify) {
            if(notification.getType().equals(type))
            {
                notification.setStatus(true);
                notificationRepository.save(notification);
            }

        }
    }

    @Override
    public void updateWatched(UUID id) {
        Optional<Notification> notification = Optional.ofNullable(notificationRepository.getById(id));
        notification.get().setStatus(true);
        notificationRepository.save(notification.get());
    }
}
