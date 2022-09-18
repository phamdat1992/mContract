package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForComment {
    private Integer id;

    private String email;
    
    private String fullName;
    
    private String avatarPath;
}
