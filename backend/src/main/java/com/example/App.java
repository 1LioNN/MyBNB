package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class App {
    static int PORT = 8000;

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        //User context
        server.createContext("/user", new User());
        server.createContext("/user/signin", new SignIn());
        server.createContext("/user/signup", new SignUp());
        server.createContext("/user/listing", new UserListings());

        //Listing context
        server.createContext("/listing", new Listing());
        //Booking context
        server.createContext("/booking", new Booking());
        //Review context
        server.createContext("/review", new Review());
        //Comment context
        server.createContext("/comment", new Comment());
        //Report context
        

        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}
