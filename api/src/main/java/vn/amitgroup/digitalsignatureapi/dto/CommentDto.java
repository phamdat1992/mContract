package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDto {
    private Integer id;
	private Date createdTime;
	private String content;
	private Float x;
	private Float y;
	private  Float page;
	private  Integer parentId;
	// private UserForComment user;
	// private SignerForComment signer;
}
