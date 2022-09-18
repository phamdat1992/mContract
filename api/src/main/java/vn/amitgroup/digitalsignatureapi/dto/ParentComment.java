package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParentComment {
    private Integer id;
	private Date createdTime;
	private String content;
	private UserForComment user;
	private SignerForComment signer;
	private Float x;
	private Float y;
	private  Float page;
	private Boolean isRead;
	private List<ChildCommentDto> childCommentDtos;
}
