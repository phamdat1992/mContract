package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    @NotBlank(message = "Full name is mandatory")
    private String fullName;
    @Pattern(regexp="(^$|[0-9]{10,13})",message = "not a phone number")
	private String phoneNumber;
    @Email(message = "not a email")
	private String email;
    @NotBlank(message = "company name is mandatory")
	private String companyName;
    @NotBlank(message = "content is mandatory")
	private String content;
}
