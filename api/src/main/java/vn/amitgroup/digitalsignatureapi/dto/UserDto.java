package vn.amitgroup.digitalsignatureapi.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;

    private String email;
    
    private String fullName;

    private String birthDate;

    private String phoneNumber;

    private String sex;

    private String idNo;

    private String issuedOn;

    private String proviceCode;

    private String districtCode;

    private String wardCode;

    private String address;
    
    private Boolean acceptEmailNotification;
    
    private String avatarPath;
}
