package vn.inspiron.mcontract.modules.FileManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileBase64DTO {
    @NotNull
    @NotEmpty
    private String data;
}
