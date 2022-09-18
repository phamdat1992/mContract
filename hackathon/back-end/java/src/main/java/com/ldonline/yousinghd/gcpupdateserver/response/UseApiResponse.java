package com.ldonline.yousinghd.gcpupdateserver.response;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;

public class UseApiResponse extends BaseApiResponse<UserEntity> {

    public UseApiResponse(ERROR error, Message message, UserEntity data) {
        super(error, message, data);
    }
}
