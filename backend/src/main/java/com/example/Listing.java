package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class Listing extends Endpoint {

    /**
     * GET /listing/:lid
     * 
     * @param lid
     * @return 200, 400, 404, 500
     *         Get basic information of listing with the given lid
     */
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }

        String lidString = splitUrl[2];
        int lid;

        try {
            lid = Integer.parseInt(lidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        ResultSet rs;

        rs = this.dao.getListingById(lid);

        if (rs == null) {
            this.sendStatus(r, 404);
            return;
        }

        String type;
        String address;
        String postal_code;
        BigDecimal lat;
        BigDecimal longi;
        String city;
        String country;
        BigDecimal price_per_day;
        String start_date;
        String end_date;
        JSONObject unavailable_dates;

        try {
            type = rs.getString("type");
            address = rs.getString("address");
            postal_code = rs.getString("postal_code");
            lat = rs.getBigDecimal("lat");
            longi = rs.getBigDecimal("longi");
            city = rs.getString("city");
            country = rs.getString("country");
            price_per_day = rs.getBigDecimal("price_per_day");
            start_date = rs.getString("start_date");
            end_date = rs.getString("end_date");
            unavailable_dates = new JSONObject(rs.getString("unavailable_dates"));

            JSONObject resp = new JSONObject();
            JSONObject data = new JSONObject();
            resp.put("idlisting", lid);
            resp.put("type", type);
            resp.put("address", address);
            resp.put("postal_code", postal_code);
            resp.put("lat", lat);
            resp.put("long", longi);
            resp.put("city", city);
            resp.put("country", country);
            resp.put("price_per_day", price_per_day);
            resp.put("start_date", start_date);
            resp.put("end_date", end_date);
            resp.put("unavailable_dates", unavailable_dates);
            data.put("data", resp);

            this.sendResponse(r, data, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
    }

    /**
     * POST /listing
     * 
     * @body uid, type, address, lat, long, ,postal code, city, country,
     *       price_per_day, start_date, end_date
     * @return 200, 400, 404, 500
     *         Create listing with the given information
     */

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] { "uid", "type", "address", "postal_code", "lat", "long", "city", "country",
                "price_per_day", "start_date", "end_date" };
        Class[] fieldClasses = new Class[] { Integer.class, String.class, String.class, String.class, BigDecimal.class,
                BigDecimal.class, String.class, String.class, BigDecimal.class, String.class, String.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
            Integer uid = body.getInt("uid");
            String type = body.getString("type");
            String address = body.getString("address");
            String postal_code = body.getString("postal_code");
            BigDecimal lat = body.getBigDecimal("lat");
            BigDecimal longi = body.getBigDecimal("long");
            String city = body.getString("city");
            String country = body.getString("country");
            BigDecimal price_per_day = body.getBigDecimal("price_per_day");
            String start_date = body.getString("start_date");
            String end_date = body.getString("end_date");

            // Check if user exists and if user is a host
            try {
                ResultSet user = this.dao.getUserById(uid);
                if (user == null) {
                    System.out.println("User does not exist");
                    this.sendStatus(r, 404);
                    return;
                }
                if (user.getString("user_type").equals("renter")) {
                    System.out.println("User is not a host");
                    this.sendStatus(r, 400);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                this.sendStatus(r, 500);
                return;
            }

            // Check if listing already exists
            if ((this.dao.getListingByAddress(address) != null) || (this.dao.getListingByLongLat(lat, longi) != null)) {
                System.out.println("Listing already exists");
                this.sendStatus(r, 400);
                return;
            }

            // Insert into listing table
            this.dao.createListing(uid, type, address, postal_code, lat, longi, city, country, price_per_day,
                    start_date, end_date);
            this.sendResponse(r, new JSONObject(), 200);
        }
    }

    /**
     * DELETE /listing/:lid
     * 
     * @param lid
     * @return 200, 400, 404, 500
     *         Delete listing with the given lid
     */

    @Override
    public void handleDelete(HttpExchange r) throws IOException, JSONException {
        // check if request url isn't malformed
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }

        // check if uid given is integer, return 400 if not
        String uidString = splitUrl[2];
        int uid;
        try {
            uid = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        ResultSet listing = this.dao.getListingById(uid);
        if (listing == null) {
            System.out.println("Listing does not exist");
            this.sendStatus(r, 404);
            return;
        }

        // delete listing
        this.dao.deleteListing(uid);
        this.sendResponse(r, new JSONObject(), 200);

    }

}
