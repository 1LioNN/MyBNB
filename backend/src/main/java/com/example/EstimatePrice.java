package com.example;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EstimatePrice extends Endpoint {
    /**
     * GET /price
     * 
     * @param uid
     * @return 200, 400, 404, 500
     *         Host toolkit
     */
    public void handleGet(HttpExchange r) throws IOException, JSONException {

        // parse request
        String query = r.getRequestURI().getQuery();
        String[] params = query.split("&");
        System.out.println(query);
        System.out.println(params.length);
        for (String param : params) {
            System.out.println(param);
        }

        // parse params
        String country = null;

        if (params.length == 0) {
            System.out.println("No params");
            this.sendStatus(r, 400);
            return;
        }

        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length != 2) {
                System.out.println("Invalid param");
                this.sendStatus(r, 400);
                return;
            }
            String key = pair[0];
            String value = pair[1];
            switch (key) {
                case "country":
                    country = value;
                    break;
                default:
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
            }
        }
        // query database

        try {
            ResultSet rs1 = this.dao.getAveragePrice(country);
            BigDecimal averagePrice = rs1.getBigDecimal("average_price");
            ResultSet rs = this.dao.getTopAmenities();

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
            JSONObject response = new JSONObject();
            response.put("average_price", averagePrice);
            response.put("top_amenities", result);
            this.sendResponse(r, response, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        // get top 5 amenities

    }
}
