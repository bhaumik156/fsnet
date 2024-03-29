package fr.univartois.ili.fsnet.commons.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

/**
 * @author FSNet
 *
 */
public final class Encryption {

    private static final Random RANDOM = new Random(new Date().getTime());

    private Encryption(){
    	
    }
    
    
    /**
     * @param key
     * @return
     */
    public static String getEncodedPassword(final String key) {
        byte[] uniqueKey;
        uniqueKey = key.getBytes();
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError("no MD5 support in this VM");
        }
        StringBuilder hashString;
        hashString = new StringBuilder();
        for (int i = 0; i < hash.length; ++i) {
            String hex;
            hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }

    /**
     * @param clearTextTestPassword
     * @param encodedActualPassword
     * @return
     */
    public static boolean testPassword(final String clearTextTestPassword,
            final String encodedActualPassword) {
        String encodedTestPassword;
        encodedTestPassword = getEncodedPassword(clearTextTestPassword);
        return encodedTestPassword.equals(encodedActualPassword);
    }

    /**
     * @return
     */
    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 6; i++) {
            int randomInt = RANDOM.nextInt(chars.length());
            password.append(chars.charAt(randomInt));
        }
        return password.toString();
    }
}
