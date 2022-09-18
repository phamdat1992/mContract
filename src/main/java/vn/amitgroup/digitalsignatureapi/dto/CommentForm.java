package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @NotNull
    private String contractId;
    @NotBlank
    private String content;
    private Integer parentId;
    private Float x;
    private Float y;
    private  Float page;
}
