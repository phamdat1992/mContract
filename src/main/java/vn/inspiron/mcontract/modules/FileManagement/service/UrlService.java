package vn.inspiron.mcontract.modules.FileManagement.service;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class UrlService {
    // TIME_TO_LIVE = 1 minute = 60000 millisecond
    private final static int TIME_TO_LIVE = 60000;

    public String generateExpirationUrl(String message, String api) {
        Date currentTime = new Date();
        long expTimeMillis = currentTime.getTime() + TIME_TO_LIVE;
        String content = expTimeMillis + "_" + message;
        String encrypt = encryptString(content);
        return api + encrypt;
    }

    public String getDataFromUrl(String encrypt) {
        String[] message = decryptString(encrypt).split("_");
        long currentTime = new Date().getTime();
        long expritionTime = Long.parseLong(message[0]);
        if (currentTime <= expritionTime) {
            return message[1];
        }
        return null;
    }

    private String encryptString(String message)
    {
        String encrypt = Base64.getEncoder().encodeToString(message.getBytes());
        return encrypt;

    }

    private String decryptString(String encodedString) {
        byte[] decryptBytes = Base64.getDecoder().decode(encodedString);
        String decrypt = new String(decryptBytes);
        return decrypt;
    }
}
