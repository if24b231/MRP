package at.fhtw;

import at.fhtw.restserver.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Integer port = 8080;
            new Server().start(port);
            System.out.printf("Server started on port %d%n", port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}