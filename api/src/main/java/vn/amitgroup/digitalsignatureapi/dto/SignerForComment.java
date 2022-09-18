package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignerForComment {
    private Integer id;
	private String fullName;
	private String email;
	private String avatarPath;
}
