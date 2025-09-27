package at.fhtw.mrp.utils;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public final class PasswordHashManager {
    public String hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash =  md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            Logger.log(LogType.ERROR, "Failed to hash password");
            return null;
        }
    }

    public Boolean checkPassword(String password, String hashedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash =  md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash).equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            Logger.log(LogType.ERROR, "Failed to check password");
            return false;
        }
    }
}
