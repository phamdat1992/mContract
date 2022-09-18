package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
    @NotBlank
    private String fullName;
    @NotBlank
    private String birthDate;
    @Pattern(regexp="(^$|[0-9]{10})",message = "not a phone")
    private String phoneNumber;
    @NotBlank
    private String sex;
    @NotBlank
    private String idNo;
    @NotBlank
    private String issuedOn;
    @NotBlank
    private String proviceCode;
    @NotBlank
    private String districtCode;
    @NotBlank
    private String wardCode;
    @NotBlank
    private String address;
    private String avatarPath;
}
