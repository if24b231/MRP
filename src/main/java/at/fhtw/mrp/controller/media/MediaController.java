package at.fhtw.mrp.controller.media;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.User;
import at.fhtw.mrp.dal.repository.AuthRepository;
import at.fhtw.mrp.dal.repository.GenreRepository;
import at.fhtw.mrp.dal.repository.MediaRepository;
import at.fhtw.mrp.model.MediaCreationDto;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.utils.PasswordHashManager;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;
import at.fhtw.restserver.server.Server;
import at.fhtw.restserver.server.tokenManagement.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

public class MediaController {
    @Controller(path = "/api/media", method = Method.POST)
    public static void create(Request request) {
        String body = request.getBody();
        if (body == null || body.isEmpty()) {
            new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\": \"Body should not be empty.\"}").send(request.getExchange());
            return;
        }

        try(MediaRepository mediaRepository = new MediaRepository(new UnitOfWork())) {
            MediaCreationDto mediaCreationDto = new ObjectMapper().readValue(body, MediaCreationDto.class);

            mediaRepository.createMedia(mediaCreationDto, request.getUserId());

            if(!mediaCreationDto.getGenres().isEmpty()) {
                try(GenreRepository genreRepository = new GenreRepository(new UnitOfWork())) {

                }
            }
        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to create media: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to login user.\"}").send(request.getExchange());
        }

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
