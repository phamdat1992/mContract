package vn.amitgroup.digitalsignatureapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import vn.amitgroup.digitalsignatureapi.dto.DownloadS3FileUrlListDto;
import vn.amitgroup.digitalsignatureapi.dto.DownloadS3FileUrlListForm;
import vn.amitgroup.digitalsignatureapi.dto.FileInformation;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.S3File;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.S3FileService;
import vn.amitgroup.digitalsignatureapi.utils.UploadUtils;
@RestController
@RequestMapping("/api/file-uploads")
public class UploadController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private S3FileService s3FileService;
    @Value("${my.domain.pdf}")
    private String url;
    @Operation(summary = "upload file")
    @PostMapping()
    public ResponseEntity<BaseResponse<FileInformation>> upload( @Parameter(
        description = "Files to be uploaded",
        example = "file selecte",
        content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        ) @RequestParam("file") MultipartFile file) throws IOException {
            String[] nameParts = file.getOriginalFilename().split("\\.(?=[^\\.]+$)");
            String fileExt = nameParts[1];
            if(!(fileExt.toLowerCase().equals("jpeg")||fileExt.toLowerCase().equals("jpg")||fileExt.toLowerCase().equals("png"))){
                throw new ApiException(ERROR.INVALIDDATA);
            }
        return ResponseEntity.ok().body(new BaseResponse<>(contractService.uploadPdf(file),HttpStatus.OK.value()) );
    }
    @Operation(summary = "upload file")
    @PostMapping("/file-convert")
    public ResponseEntity<ByteArrayResource> convert( @Parameter(
        description = "Files to be uploaded",
        example = "file selecte",
        content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        ) @RequestParam("file") MultipartFile file,HttpServletResponse resp) throws IOException,ApiException {
            if(file.isEmpty()){
                throw new ApiException(ERROR.INVALIDDATA);
            }
            if(!StringUtils.isNotBlank(file.getOriginalFilename()) ){
                throw new ApiException(ERROR.INVALIDDATA);
            }
            String[] nameParts = file.getOriginalFilename().split("\\.(?=[^\\.]+$)");
            String fileExt = nameParts[1];
            String name = file.getOriginalFilename().replaceAll("\\s", "_");
            if(fileExt.equals("docx")){
                name=name.replace(".docx",".pdf");
            }

            else{
                    name=name.replace(".doc",".pdf");
            }
            byte[] data=UploadUtils.convertToPdf(file).toByteArray();
            ByteArrayResource resource = new ByteArrayResource(data);  
            return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
                // Content-Type
                .contentType(MediaType.APPLICATION_PDF) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);  
    }
    @Operation(summary = "download file for contract")
    @GetMapping("/file-s3/{token}")
    public  ResponseEntity<InputStreamResource> downloadByToken(@PathVariable("token") String token) throws ApiException, IOException{
        Long fileId =jwtProvider.getFileId(token);
        S3File file = s3FileService.getDetail(fileId);
        URL urlFile = new URL(file.getUrl());
        Path p = Paths.get(urlFile.getPath());
        String fileName = p.getFileName().toString();
        InputStream inputStream= urlFile.openStream();
        InputStreamResource data = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                // Content-Type
                .contentType(MediaType.APPLICATION_PDF) //
                // Content-Lengh
                .contentLength(UploadUtils.getInputLength(inputStream)) //
                .body(data);  
    }
    @Operation(summary = "download file for contract")
    @PostMapping("/file-s3s")
    public  ResponseEntity<BaseResponse<DownloadS3FileUrlListDto>> downloadMulti(@RequestBody @Valid DownloadS3FileUrlListForm downloadS3FileUrlListForm){
        return ResponseEntity.ok().body(new BaseResponse<>(contractService.getMultipleUrl(downloadS3FileUrlListForm),HttpStatus.OK.value()) );
    }
}
