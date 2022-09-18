package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ContractNotify {
    private String contractId;
    private String contractName;
    private String urlContract;
    private String emailNotify;
    private Date time;
}
