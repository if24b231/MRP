package at.fhtw.restserver.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Getter
public class Request implements com.sun.net.httpserver.Request {
    private final HttpExchange exchange;
    private final String path;
    private final Map<String, List<String>> queryParameters;
    private final Map<String, String> wildcards;
    private final String body;
    private final Integer userId;

    public Request(HttpExchange exchange, String path, Map<String, List<String>> queryParameters, Map<String, String> wildcards, String body, Integer userId) {
        this.exchange = exchange;
        this.path = path;
        this.queryParameters = queryParameters;
        this.wildcards = wildcards;
        this.body = body;
        this.userId = userId;
    }

    @Override
    public URI getRequestURI() {
        return exchange.getRequestURI();
    }

    @Override
    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }

    @Override
    public Headers getRequestHeaders() {
        return exchange.getRequestHeaders();
    }
}
