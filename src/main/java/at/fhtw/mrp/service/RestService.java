package at.fhtw.mrp.service;

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

public class RestService implements HttpHandler {
    String restPattern = ":([A-Za-z0-9]+)";
    String restPatternReplacement = "([^/]+)";

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
                        if (entry.getKey().authenticationNeeded()) {
                            if (!TokenManager.INSTANCE.getInstance()
                                    .isVerified(
                                            exchange.getRequestHeaders().getFirst("Authorization").split(" ")[1]
                                    )) {
                                new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\": \"Unauthorized\"}").send(exchange);
                                return -1;
                            }
                        }
                        var wildcards = extractWildcards(entry.getKey().path(), path);
                        entry.getValue().accept(new Request(exchange, path, queryParameters, wildcards, body));
                        return 0;
                    }).findAny();
            if (result.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.code, 0);
                exchange.getResponseBody().close();
            }
        }

    private boolean matchesPath(String targetPath, String requestPath) {
        return requestPath.equals(targetPath.replaceAll(restPattern, restPatternReplacement));
    }

    private Map<String, String> extractWildcards(String targetPath, String requestPath) {
        Map<String, String> wildcards = new HashMap<>();

        //remove all Wildcards like :id from the targetPath
        Pattern pattern = Pattern.compile(restPattern);
        Matcher matcher = pattern.matcher(targetPath);
        List<String> wildcardNames = new ArrayList<>();
        while (matcher.find()) {
            wildcardNames.add(matcher.group(1));
        }

        //Convert targetPath to a regex pattern and match the requestPath
        String regex = targetPath.replaceAll(restPattern, restPatternReplacement); ///api/users/([^/]+)
        Pattern valuePattern = Pattern.compile(regex);
        Matcher valueMatcher = valuePattern.matcher(requestPath);

        if (matcher.matches()) {
            //Extract wildcard values and map them to names
            if (wildcardNames.size() == valueMatcher.groupCount()) {
                for (int i = 0; i < wildcardNames.size(); i++) {
                    wildcards.put(wildcardNames.get(i), valueMatcher.group(i + 1));
                }
            }
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
