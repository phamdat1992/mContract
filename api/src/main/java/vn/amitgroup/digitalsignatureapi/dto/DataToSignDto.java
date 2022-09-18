package vn.amitgroup.digitalsignatureapi.dto;

import eu.europa.esig.dss.model.ToBeSigned;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DataToSignDto {
    private ToBeSigned dataToSign;
    private String fileName; 
}
