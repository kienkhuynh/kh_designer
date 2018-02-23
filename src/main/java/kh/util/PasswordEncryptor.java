package kh.util;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

/*
 * This algorithm is based on the information at http://crackstation.net/hashing-security.htm#properhashing.
 */
public class PasswordEncryptor {
    /**
     * https://en.wikipedia.org/wiki/PBKDF2
     */
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final int SALT_BYTES = 16;

    public static final int HASH_BYTES = 16;

    public static String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }
    public static String hash(String password, String salt) {
        try {
        	 byte[] saltBytes = Base64.decodeBase64(salt);
             PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 10, HASH_BYTES * 2);
             SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
             return Base64.encodeBase64String(skf.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    } 
}
