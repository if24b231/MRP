package at.fhtw.restserver.server.tokenManagement;

import at.fhtw.mrp.dal.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Date;

public class TokenManager {
    private static final Algorithm algorithm = Algorithm.HMAC256("your-secret-key".getBytes());
    private final TokenStore tokenStore;

    public TokenManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String createToken(User user) {
        String token = generateJWT(user);
        this.tokenStore.createEntry(token);
        return token;
    }

    public void terminateToken(String token) {
        this.tokenStore.removeEntry(token);
    }

    public String getToken(String token) {
        return this.tokenStore.getToken(token);
    }

    public Claim getAllClaimsFromToken(String token) {

        return (Claim) getDecodedJWT(token);
    }

    private static String generateJWT(User user) {
        try {

            return JWT.create()
                    .withClaim("userId", user.getUserId())
                    .withClaim("username", user.getUsername())
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(15 * 60)))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            return null;
        }
    }

    private static DecodedJWT getDecodedJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1) //sec for nbf and iat
                    .acceptExpiresAt(5) //before expires at
                    .withIssuer("auth0")
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage());
        }
    }

    public Boolean isVarified(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1) //sec for nbf and iat
                    .acceptExpiresAt(5) //before expires at
                    .withIssuer("auth0")
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage());
        }
    }
}
