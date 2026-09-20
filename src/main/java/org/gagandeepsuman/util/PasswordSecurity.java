package org.gagandeepsuman.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordSecurity {
    // 64k iterazioni sono lo standard raccomandato oggi per PBKDF2
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * Genera l'hash combinando salt e hash nel formato: "salt_base64:hash_base64"
     * In modo da poter salvare tutto nel campo stringa `password` esistente.
     */
    public static String hashPassword(String password) {
        try {
            // Genera un Salt sicuro
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Hasha la password col salt
            byte[] hash = generateHash(password.toCharArray(), salt);
            
            // Codifica in Base64
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hash);
            
            return encodedSalt + ":" + encodedHash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Errore critico durante l'hashing della password", e);
        }
    }

    /**
     * Verifica se la password in chiaro fornita matcha con l'hash salvato sul Database
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) return false;
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            
            byte[] attemptHash = generateHash(password.toCharArray(), salt);
            
            // Confronto costante per prevenire attacchi di tipo Timing
            if (hash.length != attemptHash.length) return false;
            int diff = 0;
            for (int i = 0; i < hash.length; i++) {
                diff |= hash[i] ^ attemptHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] generateHash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }
}
