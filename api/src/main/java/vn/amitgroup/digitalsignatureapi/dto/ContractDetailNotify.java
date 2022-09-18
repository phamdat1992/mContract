package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ContractDetailNotify {
    private Integer id;
    private Date createdTime;
    private Date expirationTime;
    private String title;
    private String content;
    private Long fileId;
    private String fileName;
    private String fileUrl;
}
