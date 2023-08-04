package com.example;

import java.math.BigDecimal;
import java.sql.*;
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
    public ResultSet signupRenter(String email, String password, String name, String address, String birthday,
            String occupation, Integer SIN, String creditcard, String creditPass) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO user (email, password, name, address, dateOfBirth, occupation, SIN, user_type, credit_number, credit_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, email);
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
            return signinUser(email, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet signupHost(String email, String password, String name, String address, String birthday,
            String occupation, Integer SIN) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO user (email, password, name, address, dateOfBirth, occupation, SIN, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, email);
            query.setString(2, password);
            query.setString(3, name);
            query.setString(4, address);
            query.setString(5, birthday);
            query.setString(6, occupation);
            query.setInt(7, SIN);
            query.setString(8, "host");
            query.executeUpdate();
            return signinUser(email, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet signinUser(String email, String password) {
        try {
            // Check if email and password match
            PreparedStatement query = this.conn
                    .prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?;");
            query.setString(1, email);
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

    // Get User by email
    public ResultSet getUserbyEmail(String email) {
        try {
            // Get user information
            PreparedStatement query = this.conn.prepareStatement("SELECT * FROM user WHERE email = ?;");
            query.setString(1, email);
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
            if (rs.next()) {
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

            System.out.println(rs.getInt("idlistings"));
            System.out.println(id);

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

    public void updateListingUnavail (Integer id, String dates) {
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

    // BOOKING QUERIES

    public void createBooking(Integer uid, Integer idlisting, String start, String end, Integer status) {
        try {
            PreparedStatement query = this.conn.prepareStatement(
                    "INSERT INTO bookings (idlistings, start_date, end_date, status) VALUES (?, ?, ?, ?);");
            query.setInt(1, idlisting);
            query.setString(2, start);
            query.setString(3, end);
            query.setInt(4, status);
            query.executeUpdate();

            // Create "books" relationship
            PreparedStatement query2 = this.conn.prepareStatement(
                    "INSERT INTO books (iduser) VALUES (?);");
            query2.setInt(1, uid);
            query2.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);

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

    // REPORT QUERIES
}
