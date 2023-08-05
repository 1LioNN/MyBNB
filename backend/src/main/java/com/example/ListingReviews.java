package com.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListingReviews extends Endpoint {
    /**
     * GET /listing/review/:lid
     * 
     * @return 200, 400, 401, 404, 500
     *         Login a user into the system if the given information matches the
     *         information of the user in the database.
     */

    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        System.out.println("Handling listing reviews");
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }

        // check if uid given is integer, return 400 if not
        String uidString = splitUrl[3];
        int lid;

        try {
            lid = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        ResultSet listing = this.dao.getListingById(lid);
        if (listing == null) {
            this.sendStatus(r, 404);
            return;
        }

        try {
            ResultSet rs = this.dao.getListingReviews(lid);
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
            resp.put("reviews", result);
            resp.put("count", result.length());
            this.sendResponse(r, resp, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

    }
}
