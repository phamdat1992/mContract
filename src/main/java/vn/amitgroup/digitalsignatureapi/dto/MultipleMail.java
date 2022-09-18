package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MultipleMail {
    private String from;
    private String[] mailTo;
    private String subject;
    private Map<String,Object> props;
    private String templateName;
}
