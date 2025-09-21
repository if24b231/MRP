package at.fhtw.restserver.server.session;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String message) {
            super(message);
        }

    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidSessionException(Throwable cause) {
            super(cause);
        }
}
