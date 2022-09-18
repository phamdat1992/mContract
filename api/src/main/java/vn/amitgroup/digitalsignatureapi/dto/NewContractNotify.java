package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewContractNotify {
    private String id;
    private String type;
    private String createTimeNotify;
    private String contract;
    private String user;
    private String usernameCreateContract;
}
