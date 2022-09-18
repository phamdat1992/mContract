package com.ldonline.yousinghd.gcpupdateserver.model.entities;


import com.ldonline.common.entites.JsonEntity;
import com.ldonline.common.utils.JsonUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class UserEntity extends JsonEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
    public String username;
    public String email;
    public String password;
    public Date expired;
    public int active;
    public String deviceId;
    public String sessionId;
    public String server;
    public String type;
    public int keyActive;
    public String keyEnc;
    public int register;

    public UserEntity(){

    }

    public UserEntity(String username) {
        this.username = username;
    }

    public UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static void main(String[] args){
        UserEntity userEntity = new UserEntity("abc");
        System.out.println(userEntity);
    }
}
