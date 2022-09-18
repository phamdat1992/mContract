package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class NotifyRealTime {
    private UUID id;
    private Integer commentId;
    private String contractId;
    private String commentContent;
    private Integer parentId;
    private Float commentX;
    private Float commentY;
    private  Float commentPage;
    private String contractName;
    private String contractContent;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userAvatar;
    private Date createdTime;
    private String type;
    private Date expiredTime;
    private Integer commentParentId;
    private String signerFullName;
    private String signerEmail;
    private Integer signerId;
    private Date commentCreatedTime;
    private Boolean status;
    private String receiver;
    private Boolean isDashboard;

}
