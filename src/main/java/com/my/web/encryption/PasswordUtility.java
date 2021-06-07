package com.my.web.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password utility class. Provides methods for encryption and decryption passwords
 *
 * @author Denys Tsebro
 */
public class PasswordUtility {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int NUMBER_OF_ITERATIONS = 40000;
    private static final int KEY_LENGTH = 256;

    private PasswordUtility() {
    }

    /**
     * Generates salt of the declared length
     *
     * @param length length of generated salt
     * @return generated salt
     */
    public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    /**
     * Hash password
     *
     * @param password password to be hashed
     * @param salt     salt of the password
     * @return encoded password with PBKDF2 Algorithm
     */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, NUMBER_OF_ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password => " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Encodes password with Base64
     *
     * @param password password to be encoded
     * @param salt     salt to the password
     * @return encoded password with Base64
     */
    public static String generateSecurePassword(String password, String salt) {
        String returnValue;

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    /**
     * Verifying password with encoded one
     *
     * @param providedPassword password which need to be verified
     * @param securedPassword  encoded password
     * @param salt             salt of the encoded password
     * @return true if providedPassword is match encoded
     */
    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword, String salt) {
        boolean returnValue;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

    public static void main(String[] args) {
        String pass = "sencashierpass";
        String salt = getSalt(50);
        String secPass = generateSecurePassword(pass, salt);
        System.out.println(pass);
        System.out.println(salt);
        System.out.println(secPass);
    }

}