package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchedForm {
    @Size(min = 1)
    private List<String> contractIdList;
    @NotNull
    private Boolean status;
}
