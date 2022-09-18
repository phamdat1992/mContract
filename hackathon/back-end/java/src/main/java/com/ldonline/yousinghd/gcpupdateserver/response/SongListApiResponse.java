package com.ldonline.yousinghd.gcpupdateserver.response;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.SongEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;

import java.util.List;

public class SongListApiResponse extends BaseApiResponse<List<SongEntity>> {

    public SongListApiResponse(ERROR error, Message message, List<SongEntity> data) {
        super(error, message, data);
    }
}
