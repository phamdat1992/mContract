package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.NotBlank;

import com.google.firebase.database.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UpdateTagForm {
    @NotNull
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank  
    private String colorCode;
}
