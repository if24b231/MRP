package at.fhtw.mrp.utils;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public final class PasswordHashManager {
    SecureRandom random = new SecureRandom();

    public String hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            Logger.log(LogType.ERROR, "Failed to hash password");
            return null;
        }
    }

    public Boolean checkPassword(String password, String hashedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash).equals(hashedPassword);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            Logger.log(LogType.ERROR, "Failed to hash password");
            return false;
        }
    }
}
