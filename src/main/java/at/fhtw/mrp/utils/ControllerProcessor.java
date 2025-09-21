package at.fhtw.mrp.utils;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ControllerProcessor {
    public static void discoverControllers(String packageName, Class<? extends Annotation> annotation, Consumer<Method> callback) {
        var controllers = getClasses(packageName);

        for (Class<?> controller : controllers) {
            for (Method method : controller.getMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    callback.accept(method);
                }
            }
        }
    }

    private static List<Class<?>> getClasses(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<>();

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                URI uri = new URI(resource.toString());
                dirs.add(new File(uri.getPath()));
            }

            List<Class<?>> classes = new ArrayList<>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }

            return classes;
        } catch (ClassNotFoundException | IOException | URISyntaxException exception) {
            Logger.log(LogType.ERROR, "Couldn't find class for package: " + packageName);
        }
        return List.of();
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return List.of();
        }

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
