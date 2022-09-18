package com.ldonline.yousinghd.gcpupdateserver.service;

import com.ldonline.common.holders.ListHolder;
import com.ldonline.common.utils.CommonUtils;
import com.ldonline.yousinghd.gcpupdateserver.model.entities.SongEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.repositories.SongRepository;
import com.ldonline.yousinghd.gcpupdateserver.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongService {
    private static final Logger LOG = LoggerFactory.getLogger(SongService.class);
    public static final SongService INST = new SongService();


    @Autowired
    private SongRepository songRepository;

    public List<SongEntity> getListSongUpdate(int sVersion, String type){
        if (!CommonUtils.isEmpty(type) && type.endsWith("-vn")){
            return songRepository.getListByVersionGT(sVersion);
        } else {
            return songRepository.getListByVersionGTAndGenreN(sVersion, "20");
        }
    }
}
