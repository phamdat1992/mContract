package vn.amitgroup.digitalsignatureapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class DownloadS3FileUrlListDto {
    private List<FileInformation> fileInformations;
}
