package at.fhtw.mrp.controller.auth;

import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.repository.AuthRepository;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class AuthController {
    private AuthRepository authRepository;

    public AuthController() {
        this.authRepository = new AuthRepository(new UnitOfWork());
    }

    @Controller(path = "/api/users/register", method = Method.POST, authenticationNeeded = false)
    public void register(Request request) {
        String body = request.getBody();
        if (body == null || body.isEmpty()) {
            new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\": \"Body should not be empty.\"}");
        }

        try {
            UserCreationDto userCreationDto = new ObjectMapper().readValue(body, UserCreationDto.class);
            userCreationDto.hashPassword();

            if (this.authRepository.createUser(userCreationDto)) {
                new Response(HttpStatus.CREATED, ContentType.JSON, "User registered").send(request.getExchange());
                return;
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | SQLException | JsonProcessingException e) {
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to register user.\"}");
        }
    }

    @Controller(path = "/api/users/login", method = Method.POST, authenticationNeeded = false)
    public static void login(Request request) {
        new Response(HttpStatus.CREATED,ContentType.JSON, "{\"message\": \"success\"}").send(request.getExchange());
    }
}
