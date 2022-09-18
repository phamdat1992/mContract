package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;
import vn.amitgroup.digitalsignatureapi.entity.Category;



import java.util.Date;

@Getter
@Setter
public class NotificationResponse {

    private Integer id;
    private String title;
    private String content;
    private Date createdDate;
    private Boolean status;
    private Category category;
    private String contractId;
    private Integer userId;
    private Integer signerId;
}
