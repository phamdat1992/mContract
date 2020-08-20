package vn.inspiron.mcontract.modules.FileManagement.service;

import org.springframework.stereotype.Service;

import vn.inspiron.mcontract.modules.Exceptions.ExpiredUrlException;

import java.util.Base64;
import java.util.Date;

@Service
public class UrlService {
    // TIME_TO_LIVE = 10 minute = 600000 millisecond
    private final static int TIME_TO_LIVE = 600000;
    private final static char SEPARATOR = '@';

    public String generateExpirationCode(String message) {
        Date currentTime = new Date();
        long expTimeMillis = currentTime.getTime() + TIME_TO_LIVE;
        String content = expTimeMillis + String.valueOf(SEPARATOR) + message;
        String encrypt = encryptString(content);
        return encrypt;
    }

    public String getDataFromCode(String encrypt) throws ExpiredUrlException {
        String[] message = decryptString(encrypt);
        long currentTime = new Date().getTime();
        long expritionTime = Long.parseLong(message[0]);
        if (currentTime > expritionTime) {
            throw new ExpiredUrlException(); 
        }
        return message[1];
    }

    private String encryptString(String message)
    {
        String encrypt = Base64.getEncoder().encodeToString(message.getBytes());
        return encrypt;

    }

    private String[] decryptString(String encryptString) {
        byte[] decryptBytes = Base64.getDecoder().decode(encryptString);
        String decrypt = new String(decryptBytes);
        int separator = decrypt.indexOf(SEPARATOR);
        String time = decrypt.substring(0, separator);
        String message = decrypt.substring(separator + 1);
        String[] result = { time, message };
        return result;
    }
}
