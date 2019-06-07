package com.ilkiv.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final Logger logger = Logger.getLogger(DatabaseConnector.class);

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            logger.debug("Driver class connected");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.debug("Connection completed");
        } catch (ClassNotFoundException e) {
            logger.error("Can`t find Driver class", e);
        } catch (SQLException e) {
            logger.error("Can`t connect to database", e);
        }
        return connection;
    }
}
