package at.fhtw.restserver.server;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.annotations.Controller;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.service.RestService;
import at.fhtw.mrp.utils.ControllerProcessor;
import at.fhtw.mrp.utils.Mapping;
import at.fhtw.restserver.server.tokenManagement.TokenManager;
import at.fhtw.restserver.server.tokenManagement.TokenStore;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    public static final Map<Mapping, Consumer<Request>> controllers = new HashMap<>();

    public void start(Integer port) throws IOException {
        try {
            initControllers();
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 10);

            server.createContext("/", new RestService());
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initControllers() {
        ControllerProcessor.discoverControllers("at.fhtw.mrp.controller", Controller.class, method -> {
            var methodType = method.getAnnotation(Controller.class).method();
            var path = method.getAnnotation(Controller.class).path();
            var authenticationNeeded = method.getAnnotation(Controller.class).authenticationNeeded();

            if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != Request.class) {
                Logger.log(LogType.ERROR, String.format("Controller with method \"%s\" and path \"%s\" has invalid return or parameter types!", methodType, path));
                return;
            }

            controllers.put(new Mapping(methodType, path, authenticationNeeded), request -> invokeMethod(method, request));
            Logger.log(LogType.INFO, String.format("Registered controller \"%s\": Found Path: \"%s\" for \"%s\"", methodType, path, method.getName()));
        });
    }

    private static void invokeMethod(Method method, Request request) {
        try {
            var access = method.canAccess(null);
            if (!access) {
                method.setAccessible(true);
            }
            method.invoke(null, request);
            if (!access) {
                method.setAccessible(false);
            }
        } catch (IllegalAccessException | InvocationTargetException exception) {
            Logger.log(LogType.ERROR, String.format("%s: %s", method.getName(), exception.getLocalizedMessage()));
        }
    }
}
