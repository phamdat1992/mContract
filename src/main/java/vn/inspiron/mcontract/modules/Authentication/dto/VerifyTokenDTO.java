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
public class VerifyTokenDTO {
    @NotNull
    @NotEmpty
    private String token;
}