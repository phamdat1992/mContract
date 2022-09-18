package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ContractDto {
    private String id;
	private Date createdTime;
	private Date expirationTime;
	private String title;
	private String content;
	private String fileName;
	private String status;
	private String type;
	private Boolean wathched;
	private UserDto userCreate;
	private List<SignerDto> signerDtos;
	private List<TagDto> tagDtos;
	private Integer countCommnet;
	private Long fileId;
}
