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
        server.createContext("/user/signout", new Signout());
        server.createContext("/user/listing", new UserListings());
        server.createContext("/user/comment", new UserComments());
        server.createContext("/user/bookings", new UserBookings());
        server.createContext("/user/me", new UserSession());


        //Listing context
        server.createContext("/listing", new Listing());
        server.createContext("/listing/review", new ListingReviews());
        server.createContext("/listing/filter", new Filter());
        //Booking context
        server.createContext("/booking", new Booking());
        //Review context
        server.createContext("/review", new Review());
        //Comment context
        server.createContext("/comment", new Comment());
        //Report context
        server.createContext("/report", new Report());
        //Host toolkit 
        server.createContext("/price", new EstimatePrice());
        

        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}
