package vn.amitgroup.digitalsignatureapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailNotificationStatus {
    @NotNull
    private Boolean status;
}
