package at.fhtw.mrp.service.auth;

import at.fhtw.mrp.controller.Controller;
import at.fhtw.mrp.dal.repository.AuthRepository;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Response;

public class AuthController {
    private AuthRepository authRepository;

    public AuthController() {
        this.authRepository = new AuthRepository();
    }

    @Controller(path = "/users/register",method = Method.POST)
    public Response register(String username, String password) {

        return new Response(HttpStatus.CREATED,ContentType.JSON, "{\"message\": \"success\"}");
    }

    @Controller(path = "/api/users/login",method = Method.POST)
    public Response login(String username, String password) {

        return null;
    }
}
