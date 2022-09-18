package vn.amitgroup.digitalsignatureapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.amitgroup.digitalsignatureapi.dto.S3FileDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.S3File;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.S3FileRepository;
import vn.amitgroup.digitalsignatureapi.utils.ConvertUtil;
import vn.amitgroup.digitalsignatureapi.utils.DateUtil;
import vn.amitgroup.digitalsignatureapi.utils.S3Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class S3FileService {
    @Value("${s3.Properties.bucketName}")
    private String bucketName;
    @Value("${s3.Properties.expiredMilliSecondTime}")
    private Long expiredMilliSecondTime;
    @Autowired
    S3FileRepository s3FileRepository;
    @Autowired
    S3Utils s3Utils;

    public S3File getDetail(long s3FileId) throws ApiException {
        String s3Url = "";
        S3File s3File = null;
        Optional<S3File> s3FileOptional = s3FileRepository.findById(s3FileId);
        if (!s3FileOptional.isPresent()){
            throw ErrorCodeException.NullException();
        }
        try {
            s3File = s3FileOptional.get();
            s3Url = s3Utils.getPresignedUrl(bucketName, s3File.getKeyName(), expiredMilliSecondTime);
        } catch (Exception e){
            throw new ApiException(ERROR.S3PresignedUrl);
        }

        s3File.setUrl(s3Url);
        return s3File;
    }

    public S3FileDto uploadFile(MultipartFile multipartFile, String email) throws ApiException {
        if (multipartFile.getSize() == 0){
            throw new ApiException(ERROR.INVALIDDATA);
        }
        String s3Url = "";
        String name = multipartFile.getOriginalFilename().replaceAll("\\s", "_");
        String[] nameParts = multipartFile.getOriginalFilename().split("\\.(?=[^\\.]+$)");
        String fileExt = nameParts[1];
        if(!(fileExt.equals("docx")||fileExt.equals("pdf")||fileExt.equals("doc"))){
            throw new ApiException(ERROR.INVALIDDATA);
        }
        String keyName =
                DateUtil.getStringDateFormat("yyyyMMdd-")
                        +   UUID.randomUUID().toString() + '.' +"pdf";
        try{
            if(fileExt.equals("docx")){
                InputStream inputStream=new ByteArrayInputStream(ConvertUtil.docxToPdf(multipartFile).toByteArray());
                s3Utils.uploadFile(keyName,inputStream);
                name=name.replace(".docx",".pdf");
            }

            else{
                if(fileExt.equals("doc")){
                    InputStream inputStream=new ByteArrayInputStream(ConvertUtil.docToPdf(multipartFile).toByteArray());
                    s3Utils.uploadFile(keyName,inputStream);
                    name=name.replace(".doc",".pdf");
                }
               else{
                s3Utils.uploadFile(keyName, multipartFile.getInputStream());
               }
            }
            s3Url = s3Utils.getPresignedUrl(bucketName, keyName, expiredMilliSecondTime);
        } catch (Exception e) {
            throw new ApiException(ERROR.S3UPLOADFAIL);
        }
        S3File s3File = s3FileRepository.save(
                new S3File(bucketName, keyName, s3Url, email)
        );
        S3FileDto fileDto = new S3FileDto();
        fileDto.setId(s3File.getId());
        fileDto.setKeyName(s3File.getKeyName());
        fileDto.setBucketName(s3File.getBucketName());
        fileDto.setUrl(s3File.getUrl());
        fileDto.setEmail(s3File.getEmail());
        fileDto.setFileName(name);
        
        return fileDto;
    }

    public BaseResponse<List<S3File>> getAll(Pageable pageable) throws ApiException {
        BaseResponse<List<S3File>> response = new BaseResponse<>();
        if (pageable != null){
            Page<S3File> s3FilePage = s3FileRepository.findAll(pageable);
            response.setPageIndex(s3FilePage.getNumber());
            response.setTotal(s3FilePage.getTotalElements());
            response.setTotalPage(s3FilePage.getTotalPages());
            response.setData(s3FilePage.getContent());
        } else{
            List<S3File> s3Files = s3FileRepository.findAll();
            response.setData(s3Files);
        }
        return response;
    }
   
}
