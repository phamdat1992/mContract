package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @Email
    private String email;
    @Schema(description = "otp send to your email")
    @Pattern(regexp="(^$|[0-9]{6})",message = "not a otp code")
    private String otp;
}
