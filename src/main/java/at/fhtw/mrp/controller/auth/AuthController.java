package at.fhtw.mrp.controller.auth;

import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.dal.repository.AuthRepository;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;

public class AuthController {
    private AuthRepository authRepository;

    public AuthController() {
        this.authRepository = new AuthRepository();
    }

    @Controller(path = "/api/users/register", method = Method.POST, authenticationNeeded = false)
    public static void register(Request request) {
        new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"success\"}").send(request.getExchange());
    }

    @Controller(path = "/api/users/login", method = Method.POST, authenticationNeeded = false)
    public static void login(Request request) {
        new Response(HttpStatus.CREATED,ContentType.JSON, "{\"message\": \"success\"}").send(request.getExchange());
    }
}
