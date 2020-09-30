package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.component.CryptoUtils;
import vn.inspiron.mcontract.modules.Repository.DvhcCityRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptorAesGcmService {
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
    private final int IV_LENGTH_BYTE = 12;
    private final int SALT_LENGTH_BYTE = 16;
    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Value("${advanced-encryption-standard-password}")
    private String password;

    @Autowired
    CryptoUtils cryptoUtils;

    // return a base64 encoded AES encrypted text
    public String encrypt(byte[] pText) throws Exception {

        // 16 bytes salt
        byte[] salt = this.cryptoUtils.getRandomNonce(this.SALT_LENGTH_BYTE);

        // GCM recommended 12 bytes iv?
        byte[] iv = this.cryptoUtils.getRandomNonce(this.IV_LENGTH_BYTE);

        // secret key from password
        SecretKey aesKeyFromPassword = this.cryptoUtils.getAESKeyFromPassword(this.password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        // ASE-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(this.TAG_LENGTH_BIT, iv));
        byte[] cipherText = cipher.doFinal(pText);

        // prefix IV and Salt to cipher text
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // string representation, base64, send this string to other for decryption.
        return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    }

    // we need the same password, salt and iv to decrypt it
    private String decrypt(String cText) throws Exception {

        byte[] decode = Base64.getDecoder().decode(cText.getBytes(this.UTF_8));

        // get back the iv and salt from the cipher text
        ByteBuffer bb = ByteBuffer.wrap(decode);

        byte[] iv = new byte[this.IV_LENGTH_BYTE];
        bb.get(iv);

        byte[] salt = new byte[this.SALT_LENGTH_BYTE];
        bb.get(salt);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = this.cryptoUtils.getAESKeyFromPassword(
                this.password.toCharArray(),
                salt
        );
        Cipher cipher = Cipher.getInstance(this.ENCRYPT_ALGO);
        cipher.init(
                Cipher.DECRYPT_MODE,
                aesKeyFromPassword,
                new GCMParameterSpec(this.TAG_LENGTH_BIT, iv)
        );
        byte[] plainText = cipher.doFinal(cipherText);

        return new String(plainText, this.UTF_8);
    }
}
