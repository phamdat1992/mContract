package com.ldonline.yousinghd.gcpupdateserver.model.entities;


import javax.persistence.*;

@Entity
@Table(name = "song")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    public String HH;
    public String SC;
    public String GQID;
    public String GM;
    public String YCSD;
    public String GXM;
    public int ZS;
    public int YZ;
    public int Volume;
    public int YL;
    public String GMBH;
    public String GXBH;
    public String GXLB;
    @Column(name="\"GQLB\"")
    public String GQLB;
    public String GXID;
    public String ZYF;
    public String LY;
    public String GQLYLJ;
    public Integer WebDownload;
    public String FileSuffix;
    public String FileName;
    public String SongNameAlias;
    public String SingerNameAlias;
    public String ServerSongNotDownload;
    public int sversion;
    public String slyrics;
}
