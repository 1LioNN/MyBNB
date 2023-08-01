package com.example;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class MysqlDAO {
    //Create a DAO for mySQL database
    public Connection conn;
    public Statement stmt;

    public MysqlDAO () {
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

    //SQL Queries
    
    public ResultSet signupRenter(String email, String password, String name, String address, String birthday, String occupation, Integer SIN, String creditcard, String creditPass) {
        try {
            this.stmt.executeUpdate("INSERT INTO user (email, password, name, address, dateOfBirth, occupation, SIN, user_type, credit_number, credit_password) VALUES ('" + email + "', '" + password + "', '" + name + "', '" + address + "', '" + birthday + "', '" + occupation + "', '" + SIN + "', '" + "Renter" + "', '" + creditcard + "', '" + creditPass + "');");
            return signinUser(email, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    public ResultSet signupHost(String email, String password, String name, String address, String birthday, String occupation, Integer SIN){
         try {
            this.stmt.executeUpdate("INSERT INTO user (email, password, name, address, dateOfBirth, occupation, SIN, user_type) VALUES ('" + email + "', '" + password + "', '" + name + "', '" + address + "', '" + birthday + "', '" + occupation + "', '" + SIN + "', '" +"Host"+ "');");
            
            return signinUser(email, password);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet signinUser(String email, String password) {
        try {
            //Check if email and password match
            ResultSet rs = this.stmt.executeQuery("SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "';");
            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getUserById(Integer id){
        try {
            //Get user information
            ResultSet rs = this.stmt.executeQuery("SELECT * FROM user WHERE uid = '" + id + "';");
            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //Get User by email
    public ResultSet getUserbyEmail (String email){
        try {
            //Get user information
            ResultSet rs = this.stmt.executeQuery("SELECT * FROM user WHERE email = '" + email + "';");
            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //Get User by SIN
    public ResultSet getUserbySIN (Integer sin){
        try {
            //Get user information
            ResultSet rs = this.stmt.executeQuery("SELECT * FROM user WHERE SIN = '" + sin + "';");
            if (rs.next()) {
                return rs;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
