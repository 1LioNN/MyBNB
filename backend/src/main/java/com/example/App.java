package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class App {
    static int PORT = 8000;

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        // TODO: Add server contexts
        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}