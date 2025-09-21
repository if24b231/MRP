package at.fhtw.restserver.server.session;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.restserver.server.session.JWT.JWTManager;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, Map<ContentType, String>> sessions = new ConcurrentHashMap<>();

    public String create() {
        String sessionId = UUID.randomUUID().toString();
        String jwt = JWTManager.generateJWT(sessionId);
        sessions.put(jwt, new ConcurrentHashMap<>());
        String creationTime = LocalDateTime.now().toString();
        sessions.get(jwt).put(ContentType.SessionLastUpdated, creationTime);
        sessions.get(jwt).put(ContentType.SessionCreatedAt, creationTime);
        sessions.get(jwt).put(ContentType.session, sessionId);

        return jwt;
    }

    public void terminate(String jwt) {
        sessions.remove(jwt);
    }

    public Map<ContentType, String> getSession(String jwt) {
        try {
            return sessions.get(jwt);
        } catch (NullPointerException e) {
            Logger.log(LogType.ERROR, "Invalid jwt " + jwt + " Exception: " + e.getMessage());
        }
        return null;
    }

    public String getSessionAttribute(String jwt, ContentType contentType) {
        try {
            return getSession(jwt).get(contentType);
        } catch (NullPointerException e) {
            Logger.log(LogType.ERROR, "Invalid jwt " + jwt + " Exception: " + e.getMessage());
            return null;
        }
    }

    public void setSessionAttribute(String jwt, ContentType contentType, String value) {
        try {
            getSession(jwt).put(contentType, value);
        } catch (NullPointerException e) {
            Logger.log(LogType.ERROR, "Invalid jwt " + jwt + " Exception: " + e.getMessage());
        }
    }

    public Boolean isValid(String jwt) {
        try {
            String lastUpdated = getSession(jwt).get(ContentType.SessionLastUpdated);
            LocalDateTime now = LocalDateTime.now().minusMinutes(30);

            if (lastUpdated == null || lastUpdated.isEmpty() || now.isAfter(LocalDateTime.parse(lastUpdated))) {
                terminate(jwt);
                return false;
            }

            JWTManager.verifyJWT(jwt, sessions.get(jwt).get(ContentType.session));

            sessions.get(jwt).put(ContentType.SessionLastUpdated, LocalDateTime.now().toString());
        } catch (JWTVerificationException | NullPointerException e) {
            terminate(jwt);
            Logger.log(LogType.ERROR, "Invalid jwt " + jwt + " Exception: " + e.getMessage());
            return false;
        }

        return true;
    }
}
