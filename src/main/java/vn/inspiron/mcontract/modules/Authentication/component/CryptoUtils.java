package vn.inspiron.mcontract.modules.Authentication.component;

import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

@Component
public class CryptoUtils {

    final Integer ITERATION_COUNT = 1000;
    final Integer KEY_LENGTH = 256;
    final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    final String ALGORITHM = "AES";

    public byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // AES secret key
    public SecretKey getAESKey(int keysize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(this.ALGORITHM);
        keyGen.init(keysize, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }

    // Password derived AES 256 bits secret key
    public SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance(this.SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, salt, this.ITERATION_COUNT, this.KEY_LENGTH);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), this.ALGORITHM);
        return secret;
    }

    // hex representation
    public String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // print hex with block size split
    public String hexWithBlockSize(byte[] bytes, int blockSize) {

        String hex = this.hex(bytes);

        // one hex = 2 chars
        blockSize = blockSize * 2;

        // better idea how to print this?
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < hex.length()) {
            result.add(hex.substring(index, Math.min(index + blockSize, hex.length())));
            index += blockSize;
        }

        return result.toString();
    }
}