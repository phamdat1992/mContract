package vn.amitgroup.digitalsignatureapi.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3FileDto {
    private Long id;
    private String keyName;
    private String bucketName;
    private String url;
    private String email;
    private String fileName;
}
