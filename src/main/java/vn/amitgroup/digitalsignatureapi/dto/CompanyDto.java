package vn.amitgroup.digitalsignatureapi.dto;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CompanyDto {
    private Integer id;
	private String name;
	private String email;
    private String foundDate;
	private String phoneNumber;
	private String taxCode;
	private String proviceCode;
	private String districtCode;
	private String wardCode;
	private String address;
	private String logoPath;
}
