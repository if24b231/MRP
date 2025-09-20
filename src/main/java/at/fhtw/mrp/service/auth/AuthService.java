package at.fhtw.mrp.service.auth;

import at.fhtw.mrp.utils.ControllerProcessor;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.server.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AuthService implements HttpHandler {
    private final AuthController authController;
    private final Map<String, Method> discoveredRoutes;

    public AuthService(AuthController authController) {
        this.authController = authController;
        this.discoveredRoutes = new ControllerProcessor().discoverRoutes(authController.getClass());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        Response response = new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{ \"message\" : \"Route does not exist\" }");



        Method handlerMethod = discoveredRoutes.get(path);

        System.out.printf(handlerMethod.toString());

//        if (handlerMethod != null) {
//            try {
//
//                if(method.equals(handlerMethod)) {}
//                String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
//                        .lines().collect(Collectors.joining("\n"));
//
//                response = (Response) handlerMethod.invoke(authController, username, password);
//            } catch (Exception e) {
//                response = new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ "message" : "Internal Server Error" }");
//            }
//        }
//
        response.send(exchange);
    }
}
