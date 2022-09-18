package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import vn.amitgroup.digitalsignatureapi.entity.Role;
@Getter
@Setter
public class UserJwt {
    private Integer id;
	private String email;
	private String fullName;
	private Date createdTime;
	private String birthDate;
	private String phoneNumber;
	private String sex;
	private String idNo;
	private String issuedOn;
	private String proviceCode;
	private String districtCode;
	private String wardCode;
	private String address;
	private String avatarPath;
	private Role role;
	private Boolean acceptEmailNotification;
	private CompanyDto company;
}
