package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.Size;

@Data
public class DownloadS3FileUrlListForm {
    @Size(min = 1)
    private List<String> ids;
}
