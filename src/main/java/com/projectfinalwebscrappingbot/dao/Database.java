package com.projectfinalwebscrappingbot.dao;

import java.sql.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Database {

    @Value("${jdbcDriver}")
    private String jdbcDriver;

    @Value("${dbUrl}")
    private String dbUrl;

    @Value("${dbUsername}")
    private String user;

    @Value("${dbPassword}")
    private String pass;

    public Connection connectDatase() {
        Connection conn = null;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbUrl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public ResultSet getResultSet(Connection conn, String sql) {  //for select
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean executeQuery(Connection conn, String sql) //for insert update del
    {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void closeConnect(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            if (stmt != null) {
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
