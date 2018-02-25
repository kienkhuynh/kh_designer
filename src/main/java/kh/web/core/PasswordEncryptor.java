package kh.web.core;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * This algorithm is based on the information at http://crackstation.net/hashing-security.htm#properhashing.
 */
@Component
public class PasswordEncryptor {
    
	@Autowired ServerConfiguration config;
	
    public PasswordEncryptor() {
    	
    }
    public String hash(String password) {
        try {
        	 byte[] saltBytes = Base64.decodeBase64(config.salt);
             PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 10, 32);
              //https://en.wikipedia.org/wiki/PBKDF2
             SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
             return Base64.encodeBase64String(key.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
