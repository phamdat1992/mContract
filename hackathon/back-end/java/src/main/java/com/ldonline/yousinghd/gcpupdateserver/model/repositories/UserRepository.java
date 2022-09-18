package com.ldonline.yousinghd.gcpupdateserver.model.repositories;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity getByUsername(String username);
    UserEntity getByDeviceId(String deviceId);
}