package com.ldonline.yousinghd.gcpupdateserver.model.repositories;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.SongEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SongRepository extends CrudRepository<SongEntity, Integer> {

//      field name must is gQLB
//    public List<SongEntity> findBySversionGreaterThanAndGQLB(int sversion, String GQLB);

    @Query("SELECT s from SongEntity s where s.sversion  > :sversion and s.GQLB <> :genre")
    public List<SongEntity> getListByVersionGTAndGenreN(@Param("sversion") int sVersion, @Param("genre") String genre);

    @Query("SELECT s from SongEntity s where s.sversion  > :sversion")
    public List<SongEntity> getListByVersionGT(@Param("sversion") int sVersion);
}