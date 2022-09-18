package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Mail {
    private String from;
    private String mailTo;
    private String subject;
    private Map<String,Object> props;
    private String templateName;
}
