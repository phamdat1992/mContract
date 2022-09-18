package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMail {
    @Email
    private String email;
    
}
