package at.fhtw.restserver.server.session.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Date;

public class JWTManager {
    private static final Algorithm algorithm = Algorithm.HMAC256("your-secret-key".getBytes());

    public static String generateJWT(String sessionId) {
        try {

            return JWT.create()
                    .withClaim("sessionId", sessionId)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(15 * 60)))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            return null;
        }
    }

    public static void verifyJWT(String jwt, String sessionId) {
        try {
            DecodedJWT decodedJWT;
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("sessionId", sessionId)
                    .acceptLeeway(1) //sec for nbf and iat
                    .acceptExpiresAt(5) //before expires at
                    .withIssuer("auth0")
                    .build();

            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage());
        }
    }
}
