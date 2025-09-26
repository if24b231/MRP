package at.fhtw.restserver.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Request implements com.sun.net.httpserver.Request {
    private final HttpExchange exchange;
    private final String path;
    private final Map<String, List<String>> queryParameters;
    private final Map<String, String> wildcards;
    private final String body;

    public Request(HttpExchange exchange, String path, Map<String, List<String>> queryParameters, Map<String, String> wildcards, String body) {
        this.exchange = exchange;
        this.path = path;
        this.queryParameters = queryParameters;
        this.wildcards = wildcards;
        this.body = body;
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

    public String getBody() {
        return body;
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}
