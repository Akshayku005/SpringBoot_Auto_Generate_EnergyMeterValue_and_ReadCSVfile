package com.ecoaxis.SpringBoot.Assign.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {
    static Connection con;
    static final Logger log = LoggerFactory.getLogger(DB.class);

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.warn("Exception closing resultSet or connection" + e.getMessage());
        }
    }

    public static void closePreparedStatement(PreparedStatement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            log.warn("Exception closing resultset or statement" + e.getMessage());
        }
    }

    public static Connection createC() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = "root";
            String password = "Shivaleela@123";
            String url = "jdbc:mysql://localhost:3306/energy";
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Database Connected");
            return con;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
