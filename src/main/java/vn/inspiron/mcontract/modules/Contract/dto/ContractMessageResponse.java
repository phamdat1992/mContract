package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.inspiron.mcontract.modules.User.dto.UserResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractMessageResponse {
    private String id;
    private String mail;
    private String message;
}
