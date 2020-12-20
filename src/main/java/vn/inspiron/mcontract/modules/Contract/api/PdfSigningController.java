package vn.inspiron.mcontract.modules.Contract.api;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.validation.CertificateVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.inspiron.mcontract.modules.Authentication.model.UserAuth;
import vn.inspiron.mcontract.modules.Contract.DssWebUtils;
import vn.inspiron.mcontract.modules.Contract.dto.DataToSignDTO;
import vn.inspiron.mcontract.modules.Contract.dto.DataToSignResponse;
import vn.inspiron.mcontract.modules.Contract.dto.SignDocumentResponse;
import vn.inspiron.mcontract.modules.Contract.dto.SignatureValueDTO;
import vn.inspiron.mcontract.modules.Contract.model.SignatureDocumentForm;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Exceptions.NotFound;
import vn.inspiron.mcontract.modules.FileStorage.services.FileStorageService;
import vn.inspiron.mcontract.modules.Contract.services.SigningService;

import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.util.Date;

@RestController
public class PdfSigningController {

    private static final String SIGNED_FILES_DIR = "signed";

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    SigningService signingService;

    @PostMapping("/get-data-to-sign")
    public ResponseEntity<DataToSignResponse> getDataToSign(@RequestBody DataToSignDTO dataToSignDTO,
                                                            Authentication authentication) throws Exception {

        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        SignatureDocumentForm signaturePdfForm = buildSignatureDocumentForm(dataToSignDTO, userAuth.getUserEntity().getId());
        ToBeSigned dataToSign = signingService.getDataToSign(signaturePdfForm);
        if (dataToSign == null) {
            return null;
        }

        DataToSignResponse response = new DataToSignResponse();
        response.setDataToSign(DatatypeConverter.printBase64Binary(dataToSign.getBytes()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-document")
    public ResponseEntity<SignDocumentResponse> signDocument(@RequestBody SignatureValueDTO signatureValueDTO,
                                                             Authentication authentication) throws Exception {
        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        SignatureDocumentForm signaturePdfForm = buildSignatureDocumentForm(signatureValueDTO, userAuth.getUserEntity().getId());
        signaturePdfForm.setBase64SignatureValue(signatureValueDTO.getSignatureValue());

        DSSDocument document = signingService.signDocument(signaturePdfForm);
        InMemoryDocument signedPdfDocument = new InMemoryDocument(DSSUtils.toByteArray(document), document.getName(), document.getMimeType());

        Long fileId = fileStorageService.storeSignedDocument(signedPdfDocument, userAuth.getUserEntity().getId(), SIGNED_FILES_DIR);

        SignDocumentResponse response = new SignDocumentResponse();
        response.setFileId(fileId);
        response.setFilename(signedPdfDocument.getName());
        return ResponseEntity.ok(response);
    }

    private SignatureDocumentForm buildSignatureDocumentForm(DataToSignDTO dataToSignDTO, Long ownerId) throws Exception {
        FileEntity fileEntity;
        try {
            fileEntity = fileStorageService.loadFileEntity(dataToSignDTO.getFileId());
        } catch (FileNotFoundException e) { // File with id not found
            throw new NotFound();
        }

        // Stop user from signing other users' document
        /*if (!fileEntity.getUploadedBy().equals(ownerId)) {
            throw new NotFound();
        }

        Resource resource = fileStorageService.loadFileResource(fileEntity.getUploadPath());
        DSSDocument document = DssWebUtils.toDSSDocument(resource);

        SignatureDocumentForm signaturePdfForm = new SignatureDocumentForm();

        signaturePdfForm.setSignatureForm(SignatureForm.PAdES);
        signaturePdfForm.setSignatureLevel(SignatureLevel.PAdES_BASELINE_LTA);
        signaturePdfForm.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signaturePdfForm.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        signaturePdfForm.setBase64Certificate(dataToSignDTO.getSigningCertificate());
        signaturePdfForm.setBase64CertificateChain(dataToSignDTO.getCertificateChain());
        signaturePdfForm.setEncryptionAlgorithm(dataToSignDTO.getEncryptionAlgorithm());
        //signaturePdfForm.setSigningDate(fileEntity.getUploadedAt());
        signaturePdfForm.setDocumentToSign(document);

        return signaturePdfForm;*/
        return null;
    }

}
