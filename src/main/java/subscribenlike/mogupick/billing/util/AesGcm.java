package subscribenlike.mogupick.billing.util;

import subscribenlike.mogupick.billing.common.exception.CryptoErrorCode;
import subscribenlike.mogupick.billing.common.exception.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AesGcm {
    private static final String ALG = "AES";
    private static final String TRANS = "AES/GCM/NoPadding";
    private static final int IV_LEN = 12;  // 96-bit
    private static final int TAG_LEN = 128;

    private final byte[] keyBytes;
    private final SecureRandom random = new SecureRandom();

    public AesGcm(byte[] keyBytes) { this.keyBytes = keyBytes; }

    public String encrypt(String plain) {
        try {
            byte[] iv = new byte[IV_LEN];
            random.nextBytes(iv);
            Cipher c = Cipher.getInstance(TRANS);
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, ALG), new GCMParameterSpec(TAG_LEN, iv));
            byte[] ct = c.doFinal(plain.getBytes());
            return "v1:" + Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(ct);
        } catch (Exception e) {
            throw new CryptoException(CryptoErrorCode.AES_ENCRYPTION_FAILED, e);
        }
    }

    public String decrypt(String enc) {
        try {
            String[] parts = enc.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[1]);
            byte[] ct = Base64.getDecoder().decode(parts[2]);
            Cipher c = Cipher.getInstance(TRANS);
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALG), new GCMParameterSpec(TAG_LEN, iv));
            return new String(c.doFinal(ct));
        } catch (Exception e) {
            throw new CryptoException(CryptoErrorCode.AES_DECRYPTION_FAILED, e);
        }
    }
}
