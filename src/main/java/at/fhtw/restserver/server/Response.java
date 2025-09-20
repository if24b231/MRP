package at.fhtw.restserver.server;

import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Response {
    private final int status;
    private final String contentType;
    private final String content;

    public Response(HttpStatus httpStatus, ContentType contentType, String content) {
        this.status = httpStatus.code;
        this.contentType = contentType.type;
        this.content = content;
    }

    public void send(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add("Cache-Control", "nocache");
        httpExchange.getResponseHeaders().add("Content-Type", contentType);

        try (httpExchange) {
            byte[] responseBody = content.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(status, responseBody.length);
            httpExchange.getResponseBody().write(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
