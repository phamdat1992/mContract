package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ContractAddDto {
    private String id;

	private Date createdTime;
	
	private Date expirationTime;
	
	private String title;
	
	private String content;

	private String normalTitle;
	
	private String normalContent;
	
	private Long fileId;
	
	private Long signFileId;
	
	private String fileName;
	
	private Boolean isSign;

	private Boolean isValidTaxcode;
	
	private Date updateTime;
}
