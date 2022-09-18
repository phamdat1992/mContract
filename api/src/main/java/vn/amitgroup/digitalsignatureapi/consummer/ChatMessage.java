package vn.amitgroup.digitalsignatureapi.consummer;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessage {

    private String email;
    private String contractId;
    private int type;
}
