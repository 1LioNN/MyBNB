package main.java.com.example;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class mysqlDAO {
    //Create a DAO for mySQL database
    public Connection conn;
    public Statement stmt;

    public mysqlDAO () {
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
    
    public ResultSet query(String sql) {
        try {
            return this.stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
