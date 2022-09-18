package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.amitgroup.digitalsignatureapi.entity.ContractStatus;
@Getter
@Setter
public class ContractDetailDto {
    private String id;
    private Date createdTime;
	private Date expirationTime;
	private String title;
	private String content;
	private Long fileId;
    private String fileName;
    private String fileUrl;
    private String signFileUrl;
	private ContractStatus status;
    private UserDto userCreate;
    private CompanyDto company;
    private List<SignerDto> signerDtos;
    private List<TagDto> tagDtos;
    private List<ParentComment> commentDtos;
}
