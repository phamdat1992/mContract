package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignNotify {
    private String id;
    private String createTimeNotify;
    private String type;
    private String content;
    private String contract;//object
    private String user;//object
}
