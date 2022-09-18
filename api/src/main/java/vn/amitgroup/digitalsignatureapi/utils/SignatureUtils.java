package vn.amitgroup.digitalsignatureapi.utils;

import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.SignerTextHorizontalAlignment;
import eu.europa.esig.dss.enumerations.SignerTextPosition;
import eu.europa.esig.dss.enumerations.SignerTextVerticalAlignment;
import eu.europa.esig.dss.enumerations.VisualSignatureRotation;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.DSSFont;
import eu.europa.esig.dss.pades.DSSJavaFont;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.SignatureImageTextParameters;
import vn.amitgroup.digitalsignatureapi.entity.SignerContract;

public class SignatureUtils {

    public static PAdESSignatureParameters createParameter(X509Certificate certificate, File file,
            SignerContract signerContract, File img,List<CertificateToken> certificateTokens,String taxCode)  throws IOException {
        PDDocument pdDocument = PDDocument.load(file);
        PDPage page = pdDocument.getPage(0);
        PDRectangle pdRectangle = page.getMediaBox();
        float high = pdRectangle.getHeight();
        float width = pdRectangle.getWidth();
        Map<String, String> info = getInfo(certificate.getSubjectDN().getName());
        PAdESSignatureParameters parameters = new PAdESSignatureParameters();
        parameters.setContentSize(9472 * 2);
        parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        parameters.bLevel().setSigningDate(signerContract.getPdfSignTime());
        SignaturePackaging packaging = SignaturePackaging.ENVELOPING;
        parameters.setSignaturePackaging(packaging);
        parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        parameters.setSigningCertificate(new CertificateToken(certificate));
        parameters.setCertificateChain(certificateTokens);
        parameters.setReason("I am approving this document");
        parameters.setLocation("VN");
        SignatureImageParameters imageParameters = new SignatureImageParameters();
        if (img != null) {
            BufferedImage image = ImageIO.read(img);
            BufferedImage imageResize = resizeImage(image, 160, 160);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(imageResize, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();
            imageParameters.setImage(new InMemoryDocument(bytes));
        }
        imageParameters.setRotation(VisualSignatureRotation.AUTOMATIC);
        SignatureImageTextParameters textParameters = new SignatureImageTextParameters();
        DSSFont font = new DSSJavaFont(Font.SERIF);
        font.setSize(8);
        textParameters.setFont(font);
        textParameters.setTextColor(Color.BLACK);
        textParameters.setSignerTextHorizontalAlignment(SignerTextHorizontalAlignment.LEFT);
        textParameters.setSignerTextVerticalAlignment(SignerTextVerticalAlignment.MIDDLE);
        textParameters.setSignerTextPosition(SignerTextPosition.RIGHT);
        textParameters.setText(info.get("CN") +"\nMST: " + taxCode + "\nReason: I am approving this document "+ "\nNgười ký: "+signerContract.getSigner().getFullName()+"\nEmail: "+signerContract.getSigner().getEmail()+ "\nKý ngày: " + DateUtil.date2String(signerContract.getPdfSignTime(), "yyyy-MM-dd") + "\n"
                + DateUtil.date2String(signerContract.getPdfSignTime(), "HH:mm:ss Z"));
        imageParameters.setTextParameters(textParameters);
        imageParameters.setPage(signerContract.getPage().intValue());
        imageParameters.setxAxis(width*(signerContract.getX()/100));
        imageParameters.setyAxis(high*(signerContract.getY()/100));
        parameters.setImageParameters(imageParameters);
        return parameters;
    }

    public static SignatureValue getSignatureValue(String signed) {
        byte[] bytes = Base64.getDecoder().decode(signed.getBytes());
        SignatureValue value = new SignatureValue();
        value.setAlgorithm(SignatureAlgorithm.RSA_SHA256);
        value.setValue(bytes);
        return value;
    }

    private static Map<String, String> getInfo(String info) {
        StringTokenizer st = new StringTokenizer(info, "=,", false);
        Map<String, String> map = new HashMap<>();
        while (st.hasMoreTokens()) {
            map.put(st.nextToken().trim().replaceAll("MST:", ""), st.nextToken().trim().replaceAll("MST:", ""));
        }
        return map;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    // private static float getY(float high,int page,int totalPage,float percent){
    //     float totalHigh=totalPage*high+18*(totalPage+1);
    //     float yHigh=totalHigh*(percent/100);
    //     return  yHigh-((page-1)*high)-(page*18);
    // }
    // private static float getX(float width,float percent){
    //     float xWidth=(width+36)*(percent/100);
    //     return xWidth-16;
    // }
}
