package vn.inspiron.mcontract.modules.Authentication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String fullname;

    @NotNull
    @NotEmpty
    private String email;

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
    @JsonProperty("cmnd_issue_date")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date cmndIssueDate;

    @NotNull
    @NotEmpty
    @JsonProperty("cmnd_issue_place")
    private String cmndIssuePlace;

    @NotNull
    @NotEmpty
    private String phone;

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
