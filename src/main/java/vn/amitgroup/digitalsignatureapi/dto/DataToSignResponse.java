package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataToSignResponse {
    String dataToSign;
    String fileName;
}
