package at.fhtw.mrp.utils;

import at.fhtw.mrp.controller.Controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerProcessor {
    private final Map<String, Method> routes = new HashMap<>();

    public Map<String, Method> discoverRoutes(Class<?> controllerClass) {
        for (Method method : controllerClass.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Controller.class)) {
                Controller controllerAnnotation = method.getAnnotation(Controller.class);
                String path = controllerAnnotation.path();
                //Method httpMethod = controllerAnnotation.method(); // Assuming Method is an enum

                routes.put(path, method);
            }
        }

        return routes;
    }
}
