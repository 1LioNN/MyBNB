package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Booking extends Endpoint {

    /*
     * POST /booking
     * 
     * @body idlisting, start_date, end_date, status
     * 
     * @return 200, 400, 404, 500
     */
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] { "idlisting", "start_date", "end_date" };
        Class[] fieldClasses = new Class[] { Integer.class, String.class, String.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
            List<String> cookie = r.getRequestHeaders().get("Cookie");
            if (cookie == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 400);
                return;
            }
            String[] cookies = cookie.toString().split(";");
            Integer uid = null;
    
            for (String c : cookies) {
                String cookieName = c.replace("]", "").replace("[", "").replace(" ", "");
                if (cookieName.startsWith("session_id=")) {
                    uid = Integer.valueOf(cookieName.replace("session_id=", ""));
                }
            }
            System.out.println(uid);
            if (uid == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 400);
                return;
            }
            // Check if user is renter
            try {
                if (this.dao.getUserById(uid).getString("user_type").equals(("host"))) {
                    System.out.println("User is not a renter");
                    this.sendStatus(r, 400);
                    return;
                }
            } catch (SQLException e) {
                System.out.println("User is not a renter");
                this.sendStatus(r, 400);
                return;
            }

            Integer idlisting = body.getInt("idlisting");
            String start_date = body.getString("start_date");
            String end_date = body.getString("end_date");

            if (this.dao.getListingById(idlisting) == null) {
                System.out.println("Listing does not exist");
                this.sendStatus(r, 404);
                return;
            }

            ResultSet booked_listing = this.dao.getListingById(idlisting);
            try {
                LocalDate start = LocalDate.parse(start_date);
                LocalDate end = LocalDate.parse(end_date);
                LocalDate list_start = LocalDate.parse(booked_listing.getString("start_date"));
                LocalDate list_end = LocalDate.parse(booked_listing.getString("end_date"));
                List<LocalDate> dates = (start).datesUntil(end).collect(Collectors.toList());

                // Check if start date is in the range of the listing
                if (start.isBefore(list_start) || start.isAfter(list_end)) {
                    System.out.println("Start date is not in the range of the listing");
                    this.sendStatus(r, 400);
                    return;
                }

                // Check if start date and end date is available
                String unavailable_datesstr = booked_listing.getString("unavailable_dates")
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .replace(" ", "");
                // Check if start date is in unavailable dates
                if (unavailable_datesstr.contains(start_date) || unavailable_datesstr.contains(end_date)) {
                    System.out.println("Start date or end date is unavailable");
                    this.sendStatus(r, 400);
                    return;
                }

                String[] unavailable_dates = booked_listing.getString("unavailable_dates")
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                        .split(",");

                List<String> unavailable_dates_list = new ArrayList<String>();
                for (String date : unavailable_dates) {
                    unavailable_dates_list.add(date);
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                List<String> new_unavailable_dates = dates.stream().map(date -> "\"" + date.format(formatter) + "\"")
                        .collect(Collectors.toList());

                // Append new unavailable dates to the existing unavailable dates

                // if unavail list is not empty add it
                if (!(unavailable_dates_list.size() == 1)) {
                    new_unavailable_dates.addAll(unavailable_dates_list);
                }
                String new_unavailable_date_json = "[" + String.join(", ", new_unavailable_dates) + "]";

                BigDecimal price_per_day = booked_listing.getBigDecimal("price_per_day");
                BigDecimal total_cost = price_per_day.multiply(new BigDecimal(dates.size()));

                // Create booking
                this.dao.createBooking(uid, idlisting, start_date, end_date, 1, total_cost);
                // Update listing unavailable dates
                this.dao.updateListingUnavail(idlisting, new_unavailable_date_json);
                this.sendStatus(r, 200);

            } catch (Exception e) {
                // If e is DateTimeParseException print error message
                if (e instanceof DateTimeParseException) {
                    System.out.println("Invalid Date Format");
                    this.sendStatus(r, 400);
                    return;
                } else {
                    e.printStackTrace();
                    this.sendStatus(r, 500);
                }
            }
        }
    }

    /*
     * PATCH /booking/:bid
     * 
     * @body status
     * 
     * @return 200, 400, 404, 500
     */
    @Override
    public void handlePatch(HttpExchange r) throws IOException, JSONException {

        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }
        // check if uid given is integer, return 400 if not
        String bidString = splitUrl[2];
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] { "status" };
        Class[] fieldClasses = new Class[] { Integer.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
            List<String> cookie = r.getRequestHeaders().get("Cookie");
            if (cookie == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 400);
                return;
            }
            String[] cookies = cookie.toString().split(";");
            Integer uid = null;
    
            for (String c : cookies) {
                String cookieName = c.replace("]", "").replace("[", "").replace(" ", "");
                if (cookieName.startsWith("session_id=")) {
                    uid = Integer.valueOf(cookieName.replace("session_id=", ""));
                }
            }
            System.out.println(uid);
            if (uid == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 400);
                return;
            }

            Integer status = body.getInt("status");
            Integer bid = Integer.valueOf(bidString);
            ResultSet booking = this.dao.getBookingById(bid);
            if (booking == null) {
                System.out.println("Booking does not exist");
                this.sendStatus(r, 404);
                return;
            }
            // Status must be 1 - 5
            if (status < 1 || status > 4) {
                System.out.println("Invalid Status Code");
                this.sendStatus(r, 400);
                return;
            }
            this.dao.updateBookingStatus(bid, status);
            this.sendStatus(r, 200);
        }

    };

}
