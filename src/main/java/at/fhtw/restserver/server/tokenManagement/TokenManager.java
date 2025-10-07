package at.fhtw.restserver.server.tokenManagement;

import at.fhtw.mrp.dal.entity.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;

public enum TokenManager {
    INSTANCE(TokenStore.INSTANCE);
    private final TokenStore tokenStore;

    TokenManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String createToken(User user) {
        String tokenString = generateJWT(user);
        Token token = new Token(tokenString, LocalDateTime.now(), LocalDateTime.now());
        this.tokenStore.createEntry(token);
        return token.tokenString;
    }

    public void terminateToken(String tokenString) {
        this.tokenStore.removeEntry(tokenString);
    }

    public Token getToken(String tokenString) {
        return this.tokenStore.getToken(tokenString);
    }

    public Integer getCurrentUserId(String tokenString) {
        byte[] decodedBytes = Base64.getDecoder().decode(tokenString);
        return Integer.parseInt(new String(decodedBytes, StandardCharsets.UTF_8).split("_")[0]);
    }

    private static String generateJWT(User user) {
            String token = user.getUserId() + "_" + user.getUsername() + "_" + Instant.now();
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    public Boolean isVerified(String tokenString) {
        LocalDateTime now = LocalDateTime.now();
        Token token = getToken(tokenString);

        if (token == null) {
            return false;
        }

        if(token.lastVerified.isBefore(now.plusMinutes(15))) {
            token.lastVerified = LocalDateTime.now();
            return true;
        }

        return false;
    }
}
