package vn.amitgroup.digitalsignatureapi.consummer;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WSConnectSession {
    private UUID sessionId;
    private String accessToken;
}
