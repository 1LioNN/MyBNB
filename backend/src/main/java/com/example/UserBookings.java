package com.example;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserBookings extends Endpoint {

    /**
     * GET /user/bookings/:uid
     * 
     * @param uid
     * @return 200, 400, 404, 500
     *         Get all of a host's listings
     */
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        System.out.println("Handling get user listings request");
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }

        // check if uid given is integer, return 400 if not
        String uidString = splitUrl[3];
        int uid;
        try {
            uid = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        ResultSet user = this.dao.getUserById(uid);

        if (user == null) {
            this.sendStatus(r, 404);
            return;
        }

        ResultSet bookings = null;
        try {
            if (user.getString("user_type").equals("renter")) {
                bookings = this.dao.getUserBookings(uid);
            }

            else if (user.getString("user_type").equals("host")) {
                bookings = this.dao.getHostBookings(uid);
            }
            else {
                this.sendStatus(r, 400);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        if (bookings == null) {
            this.sendStatus(r, 404);
            return;
        }

        try {
            ResultSet rs = bookings;
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            List<String> columnNames = IntStream.range(0, columnsNumber)
                    .mapToObj(i -> {
                        try {
                            return rsmd.getColumnName(i + 1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList());

            JSONArray result = new JSONArray();
            while (rs.next()) {
                JSONObject row = new JSONObject();
                columnNames.forEach(columnName -> {
                    try {
                        row.put(columnName, rs.getObject(columnName));
                    } catch (JSONException | SQLException e) {
                        e.printStackTrace();
                    }
                });
                result.put(row);
            }
            JSONObject resp = new JSONObject();
            resp.put("bookings", result);
            resp.put("count", result.length());
            this.sendResponse(r, resp, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

    }
}
