package at.fhtw.mrp.service;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.restserver.http.ContentType;
import at.fhtw.restserver.http.HttpStatus;
import at.fhtw.restserver.server.Request;
import at.fhtw.restserver.server.Response;
import at.fhtw.restserver.server.Server;
import at.fhtw.restserver.server.tokenManagement.Token;
import at.fhtw.restserver.server.tokenManagement.TokenManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RestService implements HttpHandler {
    String restPattern = "/:[A-Za-z0-9]+";
    String restPatternReplacement = "/(.+)";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            Map<String, List<String>> queryParameters = parseQueryParameters(exchange.getRequestURI().getQuery());
            String body = new String(exchange.getRequestBody().readAllBytes());
            var result = Server.controllers.entrySet()
                    .stream()
                    .filter(entry ->
                            entry.getKey().methodType().toString().equalsIgnoreCase(method) && matchesPath(entry.getKey().path(), path))
                    .map(entry -> {
                        Integer userId = null;
                        if (entry.getKey().authenticationNeeded()) {
                            String tokenString = exchange.getRequestHeaders().getFirst("Authorization").split(" ")[1];

                            if (!TokenManager.INSTANCE.isVerified(tokenString)) {
                                new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\": \"Unauthorized\"}").send(exchange);
                                return -1;
                            }
                            userId = TokenManager.INSTANCE.getCurrentUserId(tokenString);
                        }
                        var wildcards = extractWildcards(entry.getKey().path(), path);
                        entry.getValue().accept(new Request(exchange, path, queryParameters, wildcards, body, userId));
                        return 0;
                    }).findAny();
            if (result.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.code, 0);
                exchange.getResponseBody().close();
            }
        }

    private boolean matchesPath(String targetPath, String requestPath) {
        String targetPathReplaced = targetPath.replaceAll(restPattern, restPatternReplacement);
        return requestPath.matches(targetPathReplaced);
    }

    private Map<String, String> extractWildcards(String targetPath, String requestPath) {
        Map<String, String> wildcards = new HashMap<>();

        try {
            String cleanedRequestPath = requestPath.split("\\?")[0];

            String[] splitCleanedRequestPath = cleanedRequestPath.split("/");
            String[] splitTargetPath = targetPath.split("/");

            for(int i = 0; i < splitTargetPath.length; i++) {
                if(splitTargetPath[i].contains(":")) {
                    wildcards.put(splitTargetPath[i].split(":")[1], splitCleanedRequestPath[i]);
                }
            }
        } catch (Exception ex) {
            Logger.log(LogType.ERROR, ex.getLocalizedMessage());
        }

        return wildcards;
    }

    private Map<String, List<String>> parseQueryParameters(String query) {
        Map<String, List<String>> queryParameters = new HashMap<>();
        if (query != null) {
            for (String part : query.split("&")) {
                String[] data = part.split("=");
                queryParameters.compute(data[0], (key, value) -> {
                    List<String> temp = value != null ? value : new ArrayList<>();
                    temp.add(data[1]);
                    return temp;
                });
            }
        }
        return queryParameters;
    }
}
