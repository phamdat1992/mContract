package vn.amitgroup.digitalsignatureapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.amitgroup.digitalsignatureapi.dto.SignerInfo;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.MarkAsRead;
import vn.amitgroup.digitalsignatureapi.entity.Notification;
import vn.amitgroup.digitalsignatureapi.entity.Signer;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.MarkAsReadRepository;
import vn.amitgroup.digitalsignatureapi.repository.NotificationRepository;
import vn.amitgroup.digitalsignatureapi.repository.SignerRepository;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.MarkAsReadService;
import vn.amitgroup.digitalsignatureapi.service.SignerService;
import vn.amitgroup.digitalsignatureapi.service.UserService;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MarkAsReadServiceImpl implements MarkAsReadService {

    @Autowired
    MarkAsReadRepository markAsReadRepository;
    @Autowired
    SignerService signerService;
    @Autowired
    UserService userService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    SignerRepository signerRepository;

    //Mark comments as read for signer
    @Override
    public Boolean markRead(String token, Integer commentId) throws ApiException{
        Signer signer = signerService.getSignerByToken(token);
        MarkAsRead markAsRead = markAsReadRepository.getMarkAsReadBySigner_Comment(signer.getId(),commentId);
        if(StringUtils.isEmpty(signer) ||StringUtils.isEmpty(markAsRead) )
        {
            throw ErrorCodeException.NullException();
        }
        markAsRead.setIsRead(true);
        markAsReadRepository.save(markAsRead);
        return true;
    }

    //Mark comments as read for user
    @Override
    public Boolean markReadByUser(String email, String contractId, Integer commentId) {
        SignerInfo signerInfo = new SignerInfo();
        signerInfo.setSignerEmail(email);
        signerInfo.setContracId(contractId);

        Signer signer = signerService.getSigner(signerInfo);
        MarkAsRead markAsRead = markAsReadRepository.getMarkAsReadBySigner_Comment(signer.getId(),commentId);
        if(StringUtils.isEmpty(signer) ||StringUtils.isEmpty(markAsRead) )
        {
            throw ErrorCodeException.NullException();
        }
        markAsRead.setIsRead(true);
        markAsReadRepository.save(markAsRead);
        return true;
    }

    //Get the list of notifications (type is COMMENT and SIGN) for singer
    @Override
    public List<Notification> getMarkAsBySigner(String token,Boolean isRead) throws ApiException {
        Signer signer = signerService.getSignerByToken(token);
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        List<Notification> notificationListResponse = new ArrayList<>();
        if(isRead==null)
        {
            isRead=false;
        }
        if(signer ==null)
        {
            throw ErrorCodeException.NullException();
        }
        List<MarkAsRead> markAsUnReadList = markAsReadRepository.getMarkAsBySigner(signer.getId(),isRead,signerInfo.getContracId());
        if(markAsUnReadList==null)
        {
            throw ErrorCodeException.NullException();
        }
        for (MarkAsRead markAsUnRead : markAsUnReadList) {
                Notification notification = notificationRepository.getById(markAsUnRead.getNotificationId());
                notification.setStatus(markAsUnRead.getIsRead());
                notification.setUserAvatar(markAsUnRead.getAvatarPath());
                notificationListResponse.add(notification);
        }
        notificationListResponse.sort((d1,d2) -> d2.getCreatedTime().compareTo(d1.getCreatedTime()));
//        Integer toSubList = 5;
//        if(notificationListResponse.size()<5)
//        {
//            toSubList = notificationListResponse.size();
//        }
//        return notificationListResponse.subList(0,toSubList);
        return notificationListResponse;
    }

    //Get the list of notifications (type is COMMENT and SIGN) for user
    @Override
    public List<Notification> getMarkAsByUser(String email, String contractId,Boolean isRead) {
//        User user = userService.findByEmail(email);
        SignerInfo signerInfo = new SignerInfo();
        signerInfo.setSignerEmail(email);
        signerInfo.setContracId(contractId);
        Signer signer = signerService.getSigner(signerInfo);
        if(isRead==null)
        {
            isRead=false;
        }
        if(signer ==null)
        {
            throw ErrorCodeException.NullException();
        }
        List<MarkAsRead> markAsUnReadList = markAsReadRepository.getMarkAsBySigner(signer.getId(),isRead,contractId);
        if(markAsUnReadList==null)
        {
            throw ErrorCodeException.NullException();
        }
        List<Notification> notificationListResponse = new ArrayList<>();
        for (MarkAsRead markAsUnRead : markAsUnReadList) {
            Notification notification = notificationRepository.getById(markAsUnRead.getNotificationId());
            notification.setStatus(markAsUnRead.getIsRead());
            notification.setUserAvatar(markAsUnRead.getAvatarPath());
            notificationListResponse.add(notification);
        }
        notificationListResponse.sort((d1,d2) -> d2.getCreatedTime().compareTo(d1.getCreatedTime()));
//        Integer toSubList = 5;
//        if(notificationListResponse.size()<5)
//        {
//            toSubList = notificationListResponse.size();
//        }
//        return notificationListResponse.subList(0,toSubList);
        return notificationListResponse;
    }

    //Mark notification as read for signer
    @Override
    public Boolean markReadNotificationBySigner(String token, UUID notificationId) {
        Signer signer = signerService.getSignerByToken(token);
        MarkAsRead markAsRead = markAsReadRepository.getNotificationFromMarkAsRead(notificationId,signer.getId());
        if(signer==null || markAsRead ==null)
        {
            throw ErrorCodeException.NullException();
        }
        markAsRead.setIsRead(true);
        markAsReadRepository.save(markAsRead);
        return true;
    }

    //Mark notification as read for signer
    @Override
    public Boolean markReadNotificationByUser(String email, String contractId, UUID notificationId) {
        SignerInfo signerInfo = new SignerInfo();
        signerInfo.setSignerEmail(email);
        signerInfo.setContracId(contractId);

        Signer signer = signerService.getSigner(signerInfo);
        MarkAsRead markAsRead = markAsReadRepository.getNotificationFromMarkAsRead(notificationId,signer.getId());
        if(signer==null || markAsRead ==null)
        {
            throw ErrorCodeException.NullException();
        }
        markAsRead.setIsRead(true);
        markAsReadRepository.save(markAsRead);
        return true;
    }
    //Mark all notification by user
    @Override
    public Boolean markAllNotificationByUser(String email,String type) {
        List<MarkAsRead> markAsReadList = new ArrayList<>();
        if(type==null)
        {
            markAsReadList = markAsReadRepository.getMarkAsAllByCmtAndSign(email);
        }
        else
        {
            markAsReadList = markAsReadRepository.getMarkAsByEmail(email,type);
        }
        for (MarkAsRead markAsRead : markAsReadList) {
            markAsRead.setIsRead(true);
            markAsReadRepository.save(markAsRead);
        }
        return true;
    }
    //Get all notification by user
    @Override
    public BaseResponse<List<Notification>> getAllNotification(String email, String type,Integer currentPage,Integer size) throws ApiException {
        List<MarkAsRead> markAsReadList = new ArrayList<>();
        if(type==null)
        {
            markAsReadList = markAsReadRepository.getMarkAsAllByCmtAndSign(email);
        }
        else 
        {
            markAsReadList = markAsReadRepository.getMarkAsByEmail(email,type);
        }
        long countNotify = 0;

        if(markAsReadList==null)
        {
            throw ErrorCodeException.NullException();
        }
        List<Notification> notificationListResponse = new ArrayList<>();
        BaseResponse<List<Notification>> response = new BaseResponse<>();
        for (MarkAsRead markAsRead : markAsReadList) {
            Notification notification = notificationRepository.getById(markAsRead.getNotificationId());
            notification.setStatus(markAsRead.getIsRead());
            if(!markAsRead.getIsRead())
            {
                countNotify = countNotify + 1;
            }
            notificationListResponse.add(notification);
        }
        if(currentPage!=null && size!=null && notificationListResponse.size()>0)
        {
            notificationListResponse.sort((d1,d2) -> d2.getCreatedTime().compareTo(d1.getCreatedTime()));
            Integer toSubList = (currentPage-1)*size+size;
            Integer fromSubList = (currentPage-1)*size;
            if(toSubList>notificationListResponse.size())
            {
                toSubList = notificationListResponse.size();
            }
            response.setData(notificationListResponse.subList(fromSubList,toSubList));
            response.setPageIndex(currentPage);
            response.setTotal(notificationListResponse.size());
            response.setTotalUnread(countNotify);
            response.setTotalPage((int) Math.ceil(Double.valueOf(notificationListResponse.size())/Double.valueOf(size)));
            return response;
        }
        else
        {
            notificationListResponse.sort((d1,d2) -> d2.getCreatedTime().compareTo(d1.getCreatedTime()));
            response.setData(notificationListResponse);
            response.setTotal(notificationListResponse.size());
            response.setTotalPage(1);
            return response;
        }
    }

    @Override
    public MarkAsRead getMarkAsReadByContractId(String contractId) {
        return null;
    }

    @Override
    public BaseResponse<List<MarkAsRead>> countAllNotification(String email) {
        List<MarkAsRead> markAsReadList = new ArrayList<>();
        markAsReadList = markAsReadRepository.countNotification(email);
        long countNotify = 0;
        for (MarkAsRead markAsRead : markAsReadList) {
            countNotify = countNotify + 1;
        }
        BaseResponse<List<MarkAsRead>> response = new BaseResponse<>();
        response.setData(markAsReadList);
        response.setTotal(markAsReadList.size());
        response.setTotalUnread(countNotify);
        return response;
    }
}
