package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class CommentRequest {
    @NotBlank
    private String content;
    private Integer parentId;
    private Float x;
    private Float y;
    private  Float page;
    private String email;
    private String sendFrom;
}
