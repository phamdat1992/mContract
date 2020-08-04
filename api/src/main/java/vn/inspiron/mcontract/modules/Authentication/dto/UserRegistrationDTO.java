package vn.inspiron.mcontract.modules.Authentication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String gender;

    @NotNull
    @NotEmpty
    @JsonProperty("date_of_birth")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateOfBirth;

    @NotNull
    @NotEmpty
    private String cmnd;

    @NotNull
    @NotEmpty
    private String place;

    @NotNull
    @NotEmpty
    private Date phone;

    @NotNull
    @NotEmpty
    private String street;

    @NotNull
    @NotEmpty
    private String ward;

    @NotNull
    @NotEmpty
    private String district;

    @NotNull
    @NotEmpty
    private String city;
}
