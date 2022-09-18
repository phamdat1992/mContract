package com.ldonline.yousinghd.gcpupdateserver.interceptor;

import com.ldonline.common.utils.LogUtils;
import com.ldonline.yousinghd.gcpupdateserver.exception.*;
import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import com.ldonline.yousinghd.gcpupdateserver.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActiveSecurityApiInterceptor extends BaseApiInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(UserActiveSecurityApiInterceptor.class);

    @Override
    protected boolean doPreHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        LogUtils.TabLogBuilder logBuilder = LogUtils.tabLogBuilder().addParam(userEntity);
        if (userEntity == null){
            LOG.warn(logBuilder.setMessage("Forbidden").build());
            throw new ForbiddenApiException();
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Long toDay = formatter.parse(formatter.format(new Date())).getTime();
        if (userEntity.active != 1 || userEntity.expired.getTime() < toDay) {
            int sendMailRs = EmailService.INST.sendMailExpired(userEntity.username, userEntity.deviceId);
            logBuilder.addParam(sendMailRs);
            LOG.warn(logBuilder.setMessage("InActive").build());
            throw new UserInactiveApiException();
        }

        return true;
    }
}
