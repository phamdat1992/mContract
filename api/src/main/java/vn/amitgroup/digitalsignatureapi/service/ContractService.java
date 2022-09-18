package vn.amitgroup.digitalsignatureapi.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import vn.amitgroup.digitalsignatureapi.dto.ContractAddDto;
import vn.amitgroup.digitalsignatureapi.dto.ContractDetailDto;
import vn.amitgroup.digitalsignatureapi.dto.ContractDto;
import vn.amitgroup.digitalsignatureapi.dto.ContractFrom;
import vn.amitgroup.digitalsignatureapi.dto.DataToSignForm;
import vn.amitgroup.digitalsignatureapi.dto.DataToSignResponse;
import vn.amitgroup.digitalsignatureapi.dto.DownloadS3FileUrlListDto;
import vn.amitgroup.digitalsignatureapi.dto.DownloadS3FileUrlListForm;
import vn.amitgroup.digitalsignatureapi.dto.FileInformation;
import vn.amitgroup.digitalsignatureapi.dto.SignForSignerForm;
import vn.amitgroup.digitalsignatureapi.dto.SignForm;
import vn.amitgroup.digitalsignatureapi.dto.StatisticalData;
import vn.amitgroup.digitalsignatureapi.dto.TagContractForm;
import vn.amitgroup.digitalsignatureapi.dto.WatchedForm;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface ContractService {
    Page<ContractDto> search(Map<String,String> map,Pageable page,String email);

    ContractAddDto add(ContractFrom contract,String email,MultipartFile file) throws Exception;

    ContractAddDto getById(String id);
    ContractDetailDto getContractById(String id);

    ContractDetailDto getByUser(String contractId,String email);

    ContractDetailDto getBySigner(String token);
    
    void updateWatched(String email ,WatchedForm form);

    List<StatisticalData>  getStatisticalData(String email);

    void delete( List<String> ids, String email);
    
    FileInformation uploadPdf(MultipartFile file) throws IOException;
    void addTagForContract(TagContractForm form);
    void removeTagForContract(TagContractForm form);
    void cancelContractForUser(String contractId) throws IOException, MessagingException;
    void cancelContractForSigner(HttpServletRequest request) throws IOException, MessagingException;
    void cancelContract(List<String> contractId,String email);
    void sign(SignForm signForm,String email) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, ApiException, CertificateException, IOException, MessagingException, Exception;
    void checkSigner(SignForSignerForm signForm,String token) throws CertificateException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, ApiException, IOException, Exception;
    void uploadForContract(MultipartFile file,ContractAddDto contractAddDto,String email);
    long getFileByContractId(String contractId,String email);
    DownloadS3FileUrlListDto getMultipleUrl(DownloadS3FileUrlListForm downloadS3FileUrlListForm);
    void checkSigning(String token);
    void checkSigning(String contractId,String email);

    void updateSigning(String contractId, String email, Boolean status);
    void updateSigning(String token, Boolean status);
    DataToSignResponse getDatoToSign(DataToSignForm form, String email) throws ApiException, IOException, NoSuchAlgorithmException, CertificateException, Exception;
    DataToSignResponse getDatoToSignForSigner(DataToSignForm form,String token) throws ApiException, IOException, NoSuchAlgorithmException, CertificateException, Exception;

    void updateAuthenticationFail(String contractId, String email);
    void updateAuthenticationFail(String token);
}
