package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractCancelForm {
    @Size(min = 1)
    private List<String> contractList; 
}
