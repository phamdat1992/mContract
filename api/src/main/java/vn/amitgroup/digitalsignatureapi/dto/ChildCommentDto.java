package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCommentDto {
    private Integer id;
	private Date createdTime;
	private String content;
	private Float x;
	private Float y;
	private  Float page;
	private UserForComment user;
	private SignerForComment signer;
	private Integer parentId;
	private Boolean isRead;
}
