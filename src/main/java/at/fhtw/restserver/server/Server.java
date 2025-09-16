package at.fhtw.restserver.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public void start(Integer port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 10);

        server.start();
    }
}
