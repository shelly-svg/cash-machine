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
 * Password utility class
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
     * @param length
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
     * @param password
     * @param salt
     * @return encoded password with PBKDF2 Algorithm
     */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, NUMBER_OF_ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Encodes password with Base64
     *
     * @param password
     * @param salt
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
     * @param providedPassword
     * @param securedPassword
     * @param salt
     * @return true if providedPassword is match encoded
     */
    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword, String salt) {
        boolean returnValue = false;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

    /*public static void main(String[] args) {
        String mypassword = "myadminpass";
        String salt = PasswordUtility.getSalt(50);
        System.out.println(salt);
        String mySecurePassword = PasswordUtility.generateSecurePassword(mypassword, salt);
        System.out.println(mySecurePassword);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String mySalt = "3DWY7iafZrw2w3AXRU17jU1VcG7u8gBgEOA4lz5dmIS1gD6ris";
        String mySecPass = "nolJn8lgslzedGzaLqT/HUrT7QJXioeDQO7mmvGkmV4=";
        boolean validate = PasswordUtility.verifyUserPassword(mypassword, mySecPass, mySalt);
        System.out.println(validate);
    }*/
}