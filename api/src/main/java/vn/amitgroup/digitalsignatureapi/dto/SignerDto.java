package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignerDto {
    private Integer id;
	private String fullName;
	private String email;
	private String taxCode;
	private String companyName;
	private String avatarPath;
	private Boolean isSigned;
	private Date signedTime;
	private Float x;
    private Float y;
    private  Float page;
}
