package at.fhtw.restserver.server;

import at.fhtw.mrp.service.auth.AuthController;
import at.fhtw.mrp.service.auth.AuthService;
import at.fhtw.mrp.service.user.UserService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public void start(Integer port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 10);

        server.createContext("/api/users", new AuthService(new AuthController()));
        //server.createContext("/users", new UserService());

        server.start();
    }
}
