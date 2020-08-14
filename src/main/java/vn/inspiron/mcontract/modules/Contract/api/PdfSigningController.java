package vn.inspiron.mcontract.modules.Contract.api;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ToBeSigned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Contract.DssWebUtils;
import vn.inspiron.mcontract.modules.Contract.dto.DataToSignDTO;
import vn.inspiron.mcontract.modules.Contract.dto.DataToSignResponse;
import vn.inspiron.mcontract.modules.Contract.model.SignatureDocumentForm;
import vn.inspiron.mcontract.modules.Contract.services.FileStorageService;
import vn.inspiron.mcontract.modules.Contract.services.SigningService;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@RestController
public class PdfSigningController {

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    SigningService signingService;

    @PostMapping("/get-data-to-sign")
    public ResponseEntity<DataToSignResponse> getDataToSign(@RequestBody DataToSignDTO dataToSignDTO) throws Exception {

        Resource resource = fileStorageService.loadFile(dataToSignDTO.getFileId());
        DSSDocument document = DssWebUtils.toDSSDocument(resource);

        SignatureDocumentForm signaturePdfForm = new SignatureDocumentForm();

        signaturePdfForm.setSignatureForm(SignatureForm.PAdES);
        signaturePdfForm.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        signaturePdfForm.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signaturePdfForm.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        signaturePdfForm.setBase64Certificate(dataToSignDTO.getSigningCertificate());
        signaturePdfForm.setBase64CertificateChain(dataToSignDTO.getCertificateChain());
        signaturePdfForm.setEncryptionAlgorithm(dataToSignDTO.getEncryptionAlgorithm());
        signaturePdfForm.setSigningDate(new Date());
        signaturePdfForm.setDocumentToSign(document);

        ToBeSigned dataToSign = signingService.getDataToSign(signaturePdfForm);
        if (dataToSign == null) {
            return null;
        }

        DataToSignResponse response = new DataToSignResponse();
        response.setDataToSign(DatatypeConverter.printBase64Binary(dataToSign.getBytes()));

        return ResponseEntity.ok(response);
    }

}
