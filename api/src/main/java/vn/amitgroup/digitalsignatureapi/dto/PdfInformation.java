package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PdfInformation {
    private String url;
    private String size;
    private String name;
    private Date uploadTime;
}
