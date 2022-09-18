package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagForm {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank  
    private String colorCode;
}
