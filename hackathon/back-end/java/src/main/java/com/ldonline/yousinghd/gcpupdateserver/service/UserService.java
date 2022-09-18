package com.ldonline.yousinghd.gcpupdateserver.service;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserEntity getUserByDeviceId(String deviceId){
        return userRepository.getByDeviceId(deviceId);
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.getByUsername(username);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        UserEntity save = userRepository.save(userEntity);
        return save;
    }
}
