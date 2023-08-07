package com.example;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.sql.*;

public class Report extends Endpoint {

    /*
     * 
     * GET /report/:id
     * 
     * 
     * @return 200, 404, 500
     * Encoded Reports
     * 1. # of bookings in a specific date range by city
     * 2. # of bookings in a specific date range by postal code
     * 3. # of listings per country
     * 4. # of listings per country AND city
     * 5. # of listings per country AND city AND postal code
     * 6. Rank hosts by # of listings per country
     * 7. Rank hosts by # of listings per city
     * 8. Hosts thave have more than 10% of their listings per country
     * 9. Hosts thave have more than 10% of their listings per city
     * 10. Rank Renters by Total # of bookings per specific date range
     * 11. Rank Renters by Total # of bookings per specific date range AND city (at
     * least 2 bookings)
     * 12. Largest # of cancellations per year by host
     * 13. Largest # of cancellations per year by renter
     * 14. Report of comments per listing
     */

    public void handleGet(HttpExchange r) throws IOException, JSONException {

        // check if curent user is admin(uid 1)
        // Get cookie and check if user is logged in
    
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
        if (uid != 1) {
            System.out.println("User is not admin");
            this.sendStatus(r, 400);
            return;
        }
        // parse request
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }
        // check if uid given is integer, return 400 if not
        String uidString = splitUrl[2];
        int report_type;
        try {
            report_type = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        String query = r.getRequestURI().getQuery();
        String[] params = query.split("&");
        System.out.println(query);
        System.out.println(params.length);
        for (String param : params) {
            System.out.println(param);
        }

        // parse query params
        String start_date = null;
        String end_date = null;
        String city = null;
        String postal_code = null;
        String country = null;
        String user_type = null;
        String year = null;

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
                case "start_date":
                    start_date = value;
                    break;
                case "end_date":
                    end_date = value;
                    break;
                case "city":
                    city = value;
                    break;
                case "postal_code":
                    postal_code = value;
                    break;
                case "country":
                    country = value;
                    break;
                case "user_type":
                    user_type = value;
                    break;
                case "year":
                    year = value;
                    break;
                default:
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
            }
        }
        try {
            ResultSet rep = null;
            // if reporttype is 1, query for reprot 1
            if (report_type == 1) {
                if (start_date == null || end_date == null || city == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 1
                rep = this.dao.getReport1(start_date, end_date, city);
            }
            // if reporttype is 2, query for reprot 2
            else if (report_type == 2) {
                if (start_date == null || end_date == null || postal_code == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 2
                rep = this.dao.getReport2(start_date, end_date, postal_code);
            }
            // if reporttype is 3, query for reprot 3
            else if (report_type == 3) {
                if (country == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 3
                rep = this.dao.getReport3(country);
            }
            // if reporttype is 4, query for reprot 4
            else if (report_type == 4) {
                if (country == null || city == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 4
                rep = this.dao.getReport4(country, city);
            }
            // if reporttype is 5, query for reprot 5
            else if (report_type == 5) {
                if (country == null || city == null || postal_code == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 5
                rep = this.dao.getReport5(country, city, postal_code);
            }
            // if reporttype is 6, query for reprot 6
            else if (report_type == 6) {
                if (country == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 6
                rep = this.dao.getReport6(country);
            }
            // if reporttype is 7, query for reprot 7
            else if (report_type == 7) {
                if (city == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 7
                rep = this.dao.getReport7(city);
            }
            // if reporttype is 8, query for reprot 8
            else if (report_type == 8) {
                if (country == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 8
                rep = this.dao.getReport8(country);
            }
            // if reporttype is 9, query for reprot 9
            else if (report_type == 9) {
                if (city == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 9
                rep = this.dao.getReport9(city);
            }
            // if reporttype is 10, query for reprot 10
            else if (report_type == 10) {
                if (start_date == null || end_date == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 10
                rep = this.dao.getReport10(start_date, end_date);
            }
            // if reporttype is 11, query for reprot 11
            else if (report_type == 11) {
                if (start_date == null || end_date == null || city == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 11
                rep = this.dao.getReport11(start_date, end_date, city);
            }
            // if reporttype is 12, query for reprot 12
            else if (report_type == 12 || report_type == 13) {
                if (user_type == null || year == null) {
                    System.out.println("Invalid param");
                    this.sendStatus(r, 400);
                    return;
                }
                // query for report 12
                rep = this.dao.getReport12and13(user_type, year);
            }
            // if reporttype is 14, query for reprot 14
            else if (report_type == 14) {
                rep = this.dao.getReport14();
            } else {
                System.out.println("Invalid Report Type");
                this.sendStatus(r, 400);
                return;
            }
            // if result set is null, return 404
            if (rep == null) {
                System.out.println("Nothing found");
                this.sendStatus(r, 404);
                return;
            }
            
            ResultSet rs = rep;
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
            resp.put("results", result);
            resp.put("count", result.length());
            this.sendResponse(r, resp, 200);

        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

    }
}
