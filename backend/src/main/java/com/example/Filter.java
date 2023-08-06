package com.example;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Filter extends Endpoint {

    /*
     * GET /listing/filter?<params>
     * 
     * @params longitude, latitude, start_date, end_date, distance, address,
     * min_price, max_price, postal_code
     * 
     * @body amenities (list of amenities)
     * 
     * @return 200, 400, 404, 500
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
        BigDecimal longitude = null;
        BigDecimal latitude = null;
        String start_date = null;
        String end_date = null;
        BigDecimal distance = null;
        String address = null;
        BigDecimal min_price = null;
        BigDecimal max_price = null;
        String postal_code = null;
        String orderByPrice = null;
        JSONArray amenities = null;

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
                case "longitude":
                    longitude = new BigDecimal(value);
                    break;
                case "latitude":
                    latitude = new BigDecimal(value);
                    break;
                case "start_date":
                    start_date = value;
                    break;
                case "end_date":
                    end_date = value;
                    break;
                case "distance":
                    distance = new BigDecimal(value);
                    break;
                case "address":
                    address = value;
                    break;
                case "min_price":
                    min_price = new BigDecimal(value);
                    break;
                case "max_price":
                    max_price = new BigDecimal(value);
                    break;
                case "postal_code":
                    postal_code = value;
                    break;
                case "amenities":
                    // split amenities by comma
                    amenities = new JSONArray();
                    String[] amenityList = value.split(",");
                    for (String amenity : amenityList) {
                        amenities.put(Integer.parseInt(amenity));
                    }

                    break;
                case "orderByPrice":
                    orderByPrice = value;
                    break;
                default:
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
            }
        }

        // filter listings
        try {
            ResultSet rs = this.dao.filterListings(longitude, latitude, distance, start_date, end_date, address,
                    postal_code,
                    min_price, max_price, orderByPrice, amenities);

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
            resp.put("listings", result);
            resp.put("count", result.length());
            this.sendResponse(r, resp, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

    }
}
