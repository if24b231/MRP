package at.fhtw.mrp.controller.auth;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.User;
import at.fhtw.mrp.dal.exceptions.UserAlreadyExistsException;
import at.fhtw.mrp.dal.repository.AuthRepository;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.utils.PasswordHashManager;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;
import at.fhtw.restserver.server.auth.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthController {
    @Controller(path = "/api/users/register", method = Method.POST, authenticationNeeded = false)
    public static void register(Request request) {
        String body = request.getBody();
        if (body == null || body.isEmpty()) {
            new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\": \"Body should not be empty.\"}").send(request.getExchange());
            return;
        }

        try(AuthRepository authRepository = new AuthRepository(new UnitOfWork())) {
            UserCreationDto userCreationDto = new ObjectMapper().readValue(body, UserCreationDto.class);
            userCreationDto.hashPassword();

            if (authRepository.createUser(userCreationDto)) {
                authRepository.SaveChanges();
                new Response(HttpStatus.CREATED, ContentType.JSON, null).send(request.getExchange());
            }
        } catch (UserAlreadyExistsException e) {
            new Response(HttpStatus.CONFLICT, ContentType.JSON, e.getMessage()).send(request.getExchange());
        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to register user: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to register user.\"}").send(request.getExchange());
        }
    }

    @Controller(path = "/api/users/login", method = Method.POST, authenticationNeeded = false)
    public static void login(Request request) {
        String body = request.getBody();
        if (body == null || body.isEmpty()) {
            new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\": \"Body should not be empty.\"}").send(request.getExchange());
            return;
        }

        try(AuthRepository authRepository = new AuthRepository(new UnitOfWork())) {
            UserCreationDto userCreationDto = new ObjectMapper().readValue(body, UserCreationDto.class);

            User user = authRepository.getUser(userCreationDto.getUsername());

            if (user == null) {
                authRepository.SaveChanges();
                new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\": \"User not found\"}").send(request.getExchange());
                return;
            }

            Boolean isCorrect = new PasswordHashManager().checkPassword(userCreationDto.getPassword(),  user.getPassword());

            if (isCorrect) {
                new Response(HttpStatus.OK, ContentType.JSON,
                        String.format("{\"message\": \"Login successful\", \"token\": \"%s\"}", TokenManager.INSTANCE.createToken(user)))
                        .send(request.getExchange());
                return;
            }

            new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{\"message\": \"Login not successful\"}")
                    .send(request.getExchange());

        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to login user: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to login user.\"}").send(request.getExchange());
        }
    }
}
