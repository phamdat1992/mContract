package vn.inspiron.mcontract.modules.Contract.services;

import eu.europa.esig.dss.AbstractSignatureParameters;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Contract.DssWebUtils;
import vn.inspiron.mcontract.modules.Contract.model.SignatureDocumentForm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class SigningService {

    @Autowired
    private PAdESService pAdESService;

    public ToBeSigned getDataToSign(SignatureDocumentForm form) {

        DocumentSignatureService service = pAdESService;

        AbstractSignatureParameters parameters = fillParameters(form);

        ToBeSigned toBeSigned = service.getDataToSign(form.getDocumentToSign(), parameters);

        return toBeSigned;
    }

    private AbstractSignatureParameters fillParameters(SignatureDocumentForm form) {
        PAdESSignatureParameters padesParams = new PAdESSignatureParameters();
        padesParams.setContentSize(9472 * 2); // double reserved space for signature
        AbstractSignatureParameters parameters = padesParams;
        parameters.setSignaturePackaging(form.getSignaturePackaging());

        parameters.setSignatureLevel(form.getSignatureLevel());
        parameters.setDigestAlgorithm(form.getDigestAlgorithm());
        parameters.bLevel().setSigningDate(form.getSigningDate());
        parameters.setSignWithExpiredCertificate(false);

        if (form.getContentTimestamp() != null) {
            parameters.setContentTimestamps(Arrays.asList(DssWebUtils.toTimestampToken(form.getContentTimestamp())));
        }

        CertificateToken signingCertificate = DSSUtils.loadCertificateFromBase64EncodedString(form.getBase64Certificate());
        parameters.setSigningCertificate(signingCertificate);

        List<String> base64CertificateChain = form.getBase64CertificateChain();
        if (Utils.isCollectionEmpty(base64CertificateChain)) {
            List<CertificateToken> certificateChain = new LinkedList<CertificateToken>();
            for (String base64Certificate : base64CertificateChain) {
                certificateChain.add(DSSUtils.loadCertificateFromBase64EncodedString(base64Certificate));
            }
            parameters.setCertificateChain(certificateChain);
        }

        return parameters;
    }

}
