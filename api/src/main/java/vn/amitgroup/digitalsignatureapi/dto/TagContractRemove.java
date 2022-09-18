package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TagContractRemove {
    private List<Integer> tagListId;
   private String contractId;
}
