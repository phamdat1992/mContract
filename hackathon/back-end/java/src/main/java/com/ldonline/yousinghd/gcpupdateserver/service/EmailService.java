package com.ldonline.yousinghd.gcpupdateserver.service;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    public static final EmailService INST = new EmailService();


    public int sendMailExpired(String username, String deviceId) {
        return 0;
    }
}
