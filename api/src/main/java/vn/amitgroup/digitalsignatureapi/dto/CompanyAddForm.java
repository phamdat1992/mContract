package vn.amitgroup.digitalsignatureapi.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CompanyAddForm {
	@NotBlank
	private String name;
	@Email
	private String email;
    private String foundDate;
	private String phoneNumber;
	@Pattern(regexp="(^$|[0-9]{10,13})",message = "not a tax code")
	private String taxCode;
	private String proviceCode;
	private String districtCode;
	private String wardCode;
	private String address;
	private String logoPath;
}
