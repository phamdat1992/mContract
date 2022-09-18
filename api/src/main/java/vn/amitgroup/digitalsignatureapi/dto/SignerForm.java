package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SignerForm {
    @NotBlank(message = "Full name is mandatory")
	private String fullName;
    @Email(message = "not a email")
	private String email;
    @Pattern(regexp="(^$|[0-9]{10,13})",message = "not a tax code")
	private String taxCode;
    @NotNull
    private Float x;
    @NotNull
    private Float y;
    @NotNull
    private  Float page;
}
