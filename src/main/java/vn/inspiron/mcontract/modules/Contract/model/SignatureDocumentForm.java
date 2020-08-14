package vn.inspiron.mcontract.modules.Contract.model;

import eu.europa.esig.dss.enumerations.*;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.ws.dto.TimestampDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SignatureDocumentForm {

    private Date signingDate;

    private SignatureForm signatureForm;

    private SignatureLevel signatureLevel;

    private DigestAlgorithm digestAlgorithm;

    private String base64Certificate;

    private List<String> base64CertificateChain;

    private EncryptionAlgorithm encryptionAlgorithm;

    private String base64SignatureValue;

    private TimestampDTO contentTimestamp;

    private DSSDocument documentToSign;

    private SignaturePackaging signaturePackaging;

    private ASiCContainerType containerType;
}
