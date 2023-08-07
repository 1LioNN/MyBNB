package com.example;

import java.math.BigDecimal;
import java.sql.*;

import org.json.JSONArray;

import com.sun.net.httpserver.Authenticator.Result;

import io.github.cdimascio.dotenv.Dotenv;

public class MysqlDAO {
    // Create a DAO for mySQL database
    public Connection conn;
    public Statement stmt;

    public MysqlDAO() {
        Dotenv dotenv = Dotenv.load();
        String url = "jdbc:mysql://localhost:3306/" + dotenv.get("DB_NAME");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(url, "root", dotenv.get("DB_PASS"));
            this.stmt = this.conn.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // SQL Queries

    // USER QUERIES
    public ResultSet signupRenter(String username, String password, String name, String address, String birthday,
            String occupation, Integer SIN, String creditcard, String creditPass) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO user (username, password, name, address, dateOfBirth, occupation, SIN, user_type, credit_number, credit_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, username);
            query.setString(2, password);
            query.setString(3, name);
            query.setString(4, address);
            query.setString(5, birthday);
            query.setString(6, occupation);
            query.setInt(7, SIN);
            query.setString(8, "renter");
            query.setString(9, creditcard);
            query.setString(10, creditPass);
            query.executeUpdate();
            return signinUser(username, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet signupHost(String username, String password, String name, String address, String birthday,
            String occupation, Integer SIN) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO user (username, password, name, address, dateOfBirth, occupation, SIN, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, username);
            query.setString(2, password);
            query.setString(3, name);
            query.setString(4, address);
            query.setString(5, birthday);
            query.setString(6, occupation);
            query.setInt(7, SIN);
            query.setString(8, "host");
            query.executeUpdate();
            return signinUser(username, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet signinUser(String username, String password) {
        try {
            // Check if username and password match
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?;");
            query.setString(1, username);
            query.setString(2, password);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getUserById(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM user WHERE iduser = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Get User by username
    public ResultSet getUserbyEmail(String username) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM user WHERE username = ?;");
            query.setString(1, username);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Get User by SIN
    public ResultSet getUserbySIN(Integer sin) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM user WHERE SIN = ?;");
            query.setInt(1, sin);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // DELETE USER BY ID
    public void deleteUser(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("DELETE FROM user WHERE iduser = ?;");
            query.setInt(1, id);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // LISTING QUERIES
    public ResultSet getUserListings(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement(
                    "SELECT * FROM mybnb.listings WHERE mybnb.listings.idlistings IN (SELECT idlistings FROM mybnb.hosts WHERE mybnb.hosts.iduser = ? );");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void createListing(Integer id, String type, String address, String postalCode, BigDecimal lat,
            BigDecimal longi,
            String city, String country, BigDecimal price, String start, String end) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO listings (type, address, postal_code, lat, longi, city, country, price_per_day, start_date, end_date, unavailable_dates) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, type);
            query.setString(2, address);
            query.setString(3, postalCode);
            query.setBigDecimal(4, lat);
            query.setBigDecimal(5, longi);
            query.setString(6, city);
            query.setString(7, country);
            query.setBigDecimal(8, price);
            query.setString(9, start);
            query.setString(10, end);
            // Insert empty json array for unavailable dates
            query.setString(11, "[]");

            query.executeUpdate();

            // Find newly created listing id
            PreparedStatement query2 = this.conn.prepareStatement(
                    "SELECT idlistings FROM listings WHERE address = ? AND lat = ? AND longi = ?");
            query2.setString(1, address);
            query2.setBigDecimal(2, lat);
            query2.setBigDecimal(3, longi);
            ResultSet rs = query2.executeQuery();
            rs.next();

            // Check if host listing pair already exists
            PreparedStatement query4 = this.conn.prepareStatement(
                    "SELECT * FROM hosts WHERE iduser = ? AND idlistings = ?");
            query4.setInt(1, id);
            query4.setInt(2, rs.getInt("idlistings"));
            ResultSet rs2 = query4.executeQuery();

            if (rs2.next()) {
                return;
            }
            // Add host to listing
            PreparedStatement query3 = this.conn.prepareStatement(
                    "INSERT INTO hosts (iduser, idlistings) VALUES (?, ?);");
            query3.setInt(1, id);
            query3.setInt(2, rs.getInt("idlistings"));
            query3.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public ResultSet getListingById(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM listings WHERE idlistings = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getListingByAddress(String address) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM listings WHERE address = ?;");
            query.setString(1, address);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getListingByLongLat(BigDecimal lat, BigDecimal longi) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM listings WHERE lat = ? AND longi = ?;");
            query.setBigDecimal(1, lat);
            query.setBigDecimal(2, longi);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteListing(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("DELETE FROM listings WHERE idlistings = ?;");
            query.setInt(1, id);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getUnavailableDates(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT unavailable_dates FROM listings WHERE idlistings = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void updateListingUnavail(Integer id, String dates) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("UPDATE listings SET unavailable_dates = ? WHERE idlistings = ?;");
            query.setString(1, dates);
            query.setInt(2, id);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateListing(Integer id, BigDecimal price_per_day, String start_date, String end_date) {
        String query;
        // if a field is not null, update it
        if (price_per_day != null) {
            query = "UPDATE listings SET price_per_day = ? WHERE idlistings = ?;";
            try {
                PreparedStatement stmt = this.conn.prepareStatement(query);
                stmt.setBigDecimal(1, price_per_day);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (start_date != null) {
            query = "UPDATE listings SET start_date = ? WHERE idlistings = ?;";
            try {
                PreparedStatement stmt = this.conn.prepareStatement(query);
                stmt.setString(1, start_date);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (end_date != null) {
            query = "UPDATE listings SET end_date = ? WHERE idlistings = ?;";
            try {
                PreparedStatement stmt = this.conn.prepareStatement(query);
                stmt.setString(1, end_date);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // BOOKING QUERIES

    public void createBooking(Integer uid, Integer idlisting, String start, String end, Integer status,
            BigDecimal price) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO bookings (idlistings, start_date, end_date, status, total_cost) VALUES (?, ?, ?, ?, ?);");
            query.setInt(1, idlisting);
            query.setString(2, start);
            query.setString(3, end);
            query.setInt(4, status);
            query.setBigDecimal(5, price);
            query.executeUpdate();

            // Find newly created booking id
            PreparedStatement query1 = this.conn.prepareStatement(
                    "SELECT idbookings FROM bookings WHERE idlistings = ? AND start_date = ? AND end_date = ? AND status = ? AND total_cost = ?;");
            query1.setInt(1, idlisting);
            query1.setString(2, start);
            query1.setString(3, end);
            query1.setInt(4, status);
            query1.setBigDecimal(5, price);
            ResultSet rs = query1.executeQuery();
            rs.next();
            Integer bid = rs.getInt("idbookings");

            // Create "books" relationship
            PreparedStatement query2 = this.conn.prepareStatement(
                    "INSERT INTO books (iduser, idbookings) VALUES (?, ?);");
            query2.setInt(1, uid);
            query2.setInt(2, bid);

            query2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ResultSet getBookingById(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM bookings WHERE idbookings = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void updateBookingStatus(Integer id, Integer status) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("UPDATE bookings SET status = ? WHERE idbookings = ?;");
            query.setInt(1, status);
            query.setInt(2, id);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getUserBookings(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement(
                    "SELECT * FROM mybnb.bookings WHERE mybnb.bookings.idbookings IN (SELECT idbookings FROM mybnb.books WHERE mybnb.books.iduser = ?)");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getHostBookings (Integer id){
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement(
                    "SELECT * FROM mybnb.bookings WHERE idlistings IN (SELECT idlistings FROM mybnb.hosts WHERE iduser = ?)");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // REVIEWS QUERIES

    public void createReview(Integer iduser, Integer idlisting, BigDecimal rating, String content) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO review (iduser, idlistings, content, rating) VALUES (?, ?, ?, ?);");
            query.setInt(1, iduser);
            query.setInt(2, idlisting);
            query.setString(3, content);
            query.setBigDecimal(4, rating);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public ResultSet getReviewById(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM review WHERE idreview = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReviewByKey(Integer uid, Integer lid) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM review WHERE iduser = ? AND idlistings = ?;");
            query.setInt(1, uid);
            query.setInt(2, lid);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteReview(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("DELETE FROM review WHERE idreview = ?;");
            query.setInt(1, id);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getListingReviews(Integer lid) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM review WHERE idlistings = ?;");
            query.setInt(1, lid);
            ResultSet rs = query.executeQuery();

            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // COMMENT QUERIES

    public void createComment(Integer iduser, Integer iduser2, BigDecimal rating, String content) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO comment (commenter, commentee, content, rating) VALUES (?, ?, ?, ?);");
            query.setInt(1, iduser);
            query.setInt(2, iduser2);
            query.setString(3, content);
            query.setBigDecimal(4, rating);
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getCommentById(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM comment WHERE idcomment = ?;");
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getCommentByKey(Integer uid, Integer uid2) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM comment WHERE commenter = ? AND commentee = ?;");
            query.setInt(1, uid);
            query.setInt(2, uid2);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteComment(Integer id) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("DELETE FROM comment WHERE idcomment = ?;");
            query.setInt(1, id);
            query.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getUserComments(Integer uid) {
        try {
            // Get user information
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM mybnb.comment WHERE mybnb.comment.commentee = ?;");
            query.setInt(1, uid);
            ResultSet rs = query.executeQuery();

            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet filterListings(BigDecimal latitude, BigDecimal longitude, BigDecimal distance,
            String startDate, String endDate, String address,
            String postalCode, BigDecimal minPrice, BigDecimal maxPrice,
            String orderByPrice, JSONArray amenities) throws SQLException {

        StringBuilder query = new StringBuilder("SELECT * FROM listings WHERE 1=1 ");

        if (latitude != null && longitude != null && distance != null) {
            query.append(
                    " AND (6371 * acos(cos(radians(lat)) * cos(radians(?)) * cos(radians(? - longi)) + sin(radians(lat)) * sin(radians(?))) <= ?)");
        }

        if (startDate != null) {
            query.append(" AND start_date >= ?");
        }

        if (endDate != null) {
            query.append(" AND end_date <= ?");
        }

        if (address != null) {
            query.append(" AND address = ?");
        }

        if (postalCode != null) {
            query.append(" AND postal_code LIKE CONCAT(SUBSTRING(?, 1, 5), '%')");
        }

        if (minPrice != null) {
            query.append(" AND price >= ?");
        }

        if (maxPrice != null) {
            query.append(" AND price <= ?");
        }

        if (amenities != null) {
            for (int i = 0; i < amenities.length(); i++) {
                query.append(
                        "AND ? IN (SELECT idamenities FROM mybnb.amenities WHERE mybnb.amenities.idamenities IN (SELECT idamenities FROM mybnb.has WHERE mybnb.has.idlistings = mybnb.listings.idlistings) )");
            }
        }

        if (orderByPrice != null && (orderByPrice.equalsIgnoreCase("asc") || orderByPrice.equalsIgnoreCase("desc"))) {
            query.append(" ORDER BY price ").append(orderByPrice.toUpperCase());
        }

        PreparedStatement stmt = this.conn.prepareStatement(query.toString());
    
        int parameterIndex = 1;
        if (latitude != null && longitude != null && distance != null) {
            stmt.setBigDecimal(parameterIndex++, latitude);
            stmt.setBigDecimal(parameterIndex++, longitude);
            stmt.setBigDecimal(parameterIndex++, longitude);
            stmt.setBigDecimal(parameterIndex++, distance);
        }

        if (startDate != null) {
            stmt.setString(parameterIndex++, startDate);
        }

        if (endDate != null) {
            stmt.setString(parameterIndex++, endDate);
        }

        if (address != null) {
            stmt.setString(parameterIndex++, address);
        }

        if (postalCode != null) {
            stmt.setString(parameterIndex++, postalCode);
        }

        if (minPrice != null) {
            stmt.setBigDecimal(parameterIndex++, minPrice);
        }

        if (maxPrice != null) {
            stmt.setBigDecimal(parameterIndex++, maxPrice);
        }
        if (amenities != null) {
            for (int i = 0; i < amenities.length(); i++) {
                stmt.setInt(parameterIndex++, amenities.getInt(i));
            }
        }
        System.out.println(stmt.toString());
        return stmt.executeQuery();
    }
    // REPORT QUERIES

    public ResultSet getReport1(String start_date, String end_date, String city) {

        String query = "SELECT COUNT(idbookings) AS Total FROM mybnb.bookings WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.city = ?);";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, end_date);
            stmt.setString(2, start_date);
            stmt.setString(3, city);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport2(String start_date, String end_date, String postal_code) {

        String query = "SELECT COUNT(idbookings) AS Total FROM mybnb.bookings WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.postal_code = ?;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, end_date);
            stmt.setString(2, start_date);
            stmt.setString(3, postal_code);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport3(String country) {
        String query = "SELECT COUNT(idlistings) AS Total FROM mybnb.listings WHERE mybnb.listings.country = ?;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public ResultSet getReport4(String country, String city) {
        String query = "SELECT COUNT(idlistings) AS Total FROM mybnb.listings WHERE mybnb.listings.country = ? AND mybnb.listings.city = ?;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, country);
            stmt.setString(2, city);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport5(String country, String city, String postal_code) {
        String query = "SELECT COUNT(idlistings) AS Total FROM mybnb.listings WHERE mybnb.listings.country = ? AND mybnb.listings.city = ? AND mybnb.listings.postal_code = ?;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, country);
            stmt.setString(2, city);
            stmt.setString(3, postal_code);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public ResultSet getReport6(String country) {
        String query = "SELECT iduser,count(*) AS Total FROM mybnb.hosts WHERE mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.country = ?) GROUP BY iduser";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport7(String city) {
        String query = "SELECT iduser,count(*) AS Total FROM mybnb.hosts WHERE mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.city = ?) GROUP BY iduser";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport8(String country) {
        String query = "SELECT * FROM (" +
                "  SELECT " +
                "    iduser, " +
                "    COUNT(*) / (SELECT COUNT(idlistings) AS Total FROM mybnb.hosts WHERE mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.country = ?)) * 100 AS Percentage "
                +
                "  FROM " +
                "    mybnb.hosts " +
                "  WHERE " +
                "    mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.country = ?) "
                +
                "  GROUP BY " +
                "    iduser " +
                ") AS host_percentage " +
                "WHERE " +
                "  Percentage > 10.0;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, country);
            stmt.setString(2, country);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport9(String city) {
        String query = "SELECT * FROM (" +
                "  SELECT " +
                "    iduser, " +
                "    COUNT(*) / (SELECT COUNT(idlistings) AS Total FROM mybnb.hosts WHERE mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.city = ?)) * 100 AS Percentage "
                +
                "  FROM " +
                "    mybnb.hosts " +
                "  WHERE " +
                "    mybnb.hosts.idlistings IN (SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.city = ?) "
                +
                "  GROUP BY " +
                "    iduser " +
                ") AS host_percentage " +
                "WHERE " +
                "  Percentage > 10.0;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, city);
            stmt.setString(2, city);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getReport10(String start_date, String end_date) {
        String query = "SELECT iduser, COUNT(*) AS Total " +
                "FROM mybnb.books " +
                "WHERE mybnb.books.idbookings IN (" +
                "    SELECT idbookings FROM mybnb.bookings " +
                "    WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ?" +
                ") " +
                "GROUP BY iduser;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, end_date);
            stmt.setString(2, start_date);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public ResultSet getReport11(String start_date, String end_date, String city) {
        String query = "SELECT * FROM " +
                "(SELECT iduser, COUNT(*) AS Total " +
                "FROM mybnb.books " +
                "WHERE mybnb.books.idbookings IN (" +
                "    SELECT idbookings FROM mybnb.bookings " +
                "    WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? " +
                "    AND mybnb.bookings.idlistings IN (" +
                "        SELECT idlistings FROM mybnb.listings WHERE mybnb.listings.city = ?" +
                "    )" +
                ") " +
                "GROUP BY iduser) AS renter_rank " +
                "WHERE Total > 1;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, end_date);
            stmt.setString(2, start_date);
            stmt.setString(3, city);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public ResultSet getReport12and13(String user_type, String year) {
        String query = "";
        if (user_type.equals("host")) {
            query = "SELECT * FROM mybnb.user WHERE mybnb.user.iduser IN " +
                    "(SELECT iduser FROM mybnb.hosts WHERE mybnb.hosts.idlistings = " +
                    "(SELECT idlistings FROM " +
                    "(SELECT idlistings, COUNT(*) AS Total " +
                    "FROM mybnb.bookings " +
                    "WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.status = 3 "
                    +
                    "GROUP BY idlistings) AS host_cancel WHERE Total = " +
                    "(SELECT MAX(Total) AS Amount FROM " +
                    "(SELECT idlistings, COUNT(*) AS Total " +
                    "FROM mybnb.bookings " +
                    "WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.status = 3 "
                    +
                    "GROUP BY idlistings) AS max_cancel)))";
        } else if (user_type.equals("renter")) {
            query = "SELECT * FROM mybnb.user WHERE mybnb.user.iduser IN " +
                    "(SELECT iduser FROM " +
                    "(SELECT iduser, COUNT(*) AS Total " +
                    "FROM mybnb.books " +
                    "WHERE mybnb.books.idbookings IN " +
                    "(SELECT idbookings FROM mybnb.bookings WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.status = 2) "
                    +
                    "GROUP BY iduser) AS renter_cancel WHERE Total = " +
                    "(SELECT MAX(Total) AS Amount FROM " +
                    "(SELECT iduser, COUNT(*) AS Total " +
                    "FROM mybnb.books " +
                    "WHERE mybnb.books.idbookings IN " +
                    "(SELECT idbookings FROM mybnb.bookings WHERE mybnb.bookings.end_date <= ? AND mybnb.bookings.start_date >= ? AND mybnb.bookings.status = 2) "
                    +
                    "GROUP BY iduser) AS max_cancel))";

        } else {
            return null;
        }
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, year + "-12-31");
            stmt.setString(2, year + "-01-01");
            stmt.setString(3, year + "-12-31");
            stmt.setString(4, year + "-01-01");
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public ResultSet getReport14() {
        String query = "SELECT idlistings, content FROM mybnb.review order by idlistings;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs != null) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getAmenityById(Integer id) {
        String query = "SELECT * FROM mybnb.amenities WHERE idamenities = ?;";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void createAmenity(Integer id, Integer amenityid) {
        String query = "INSERT INTO mybnb.has (idlistings, idamenities) VALUES (?, ?);";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setInt(2, amenityid);
            stmt.executeUpdate();

            // Increment the number of amenities in amenities table
            String query2 = "UPDATE mybnb.amenities SET amount = amount + 1 WHERE idamenities = ?;";
            stmt = this.conn.prepareStatement(query2);
            stmt.setInt(1, amenityid);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
