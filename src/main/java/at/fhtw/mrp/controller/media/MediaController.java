package at.fhtw.mrp.controller.media;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.Media;
import at.fhtw.mrp.dal.exceptions.ForbiddenException;
import at.fhtw.mrp.dal.exceptions.NotFoundException;
import at.fhtw.mrp.dal.repository.MediaRepository;
import at.fhtw.mrp.model.MediaCreationDto;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.http.Method;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

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

            Media createdMedia = mediaRepository.createMedia(mediaCreationDto, request.getUserId());

            new Response(HttpStatus.CREATED, ContentType.JSON, new ObjectMapper().writeValueAsString(createdMedia)).send(request.getExchange());
        } catch (NullPointerException ex) {
            new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\": \"mediaCreationDto cannot be null.\"}").send(request.getExchange());
        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to create media: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to create media.\"}").send(request.getExchange());
        }
    }

    @Controller(path = "/api/media/:id", method = Method.DELETE)
    public static void delete(Request request) {
        try(MediaRepository mediaRepository = new MediaRepository(new UnitOfWork())) {
            mediaRepository.deleteMedia(Integer.parseInt(request.getQueryParameters().get("id").getFirst()),request.getUserId());

            new Response(HttpStatus.OK, ContentType.JSON, null).send(request.getExchange());
        } catch(ForbiddenException ex) {
            new Response(HttpStatus.FORBIDDEN, ContentType.JSON, null).send(request.getExchange());
        } catch(NotFoundException ex) {
            new Response(HttpStatus.NOT_FOUND, ContentType.JSON, null).send(request.getExchange());
        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to find media: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to find media.\"}").send(request.getExchange());
        }

    }

    @Controller(path = "/api/media/:id", method = Method.PUT)
    public static void update(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media", method = Method.GET)
    public static void search(Request request) {
        new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{\"message\": \"Not implemented.\"}").send(request.getExchange());
    }

    @Controller(path = "/api/media/:id", method = Method.POST)
    public static void getMediaById(Request request) {
        try(MediaRepository mediaRepository = new MediaRepository(new UnitOfWork())) {
            Media foundMedia = mediaRepository.getMedia(Integer.parseInt(request.getQueryParameters().get("id").getFirst()));

            new Response(HttpStatus.OK, ContentType.JSON, new ObjectMapper().writeValueAsString(foundMedia)).send(request.getExchange());
        } catch(NotFoundException ex) {
            new Response(HttpStatus.NOT_FOUND, ContentType.JSON, null).send(request.getExchange());
        } catch (Exception e) {
            Logger.log(LogType.ERROR, "Failed to find media: " + e.getLocalizedMessage());
            new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\": \"Failed to find media.\"}").send(request.getExchange());
        }
    }
}
