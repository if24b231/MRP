package at.fhtw.mrp.controller.media;

import at.fhtw.mrp.annotations.Controller;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;

public class MediaController {
    @Controller(path = "/api/media", method = Method.POST)
    public static void create(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media/:id", method = Method.DELETE)
    public static void delete(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media/:id", method = Method.PUT)
    public static void update(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media", method = Method.GET)
    public static void search(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media", method = Method.POST)
    public static void getMediaById(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }
}
