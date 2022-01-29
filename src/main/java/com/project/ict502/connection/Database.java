package com.project.ict502.connection;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static Connection conn = null;
    private static final String currentDb = "oracle"; // oracle or postgress

    public static String getDbType() {
        return currentDb;
    }

    private static void initPostgres() {
        try {
            Class.forName("org.postgresql.Driver"); // use postgres driver

            URI dbURI = null;
            if(System.getenv("DATABASE_URL") != null)
                dbURI = new URI(System.getenv("DATABASE_URL")); // get value from system environment
            else
                dbURI = new URI("postgres://postgres:system@localhost:5432/salizacafe");

            String username, password, dbURL;

            username = dbURI.getUserInfo().split(":")[0];
            password = dbURI.getUserInfo().split(":")[1];
            dbURL = "jdbc:postgresql://" + dbURI.getHost() + ":" + dbURI.getPort() + dbURI.getPath();

            conn = DriverManager.getConnection(dbURL, username, password); // assign to static variable
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void initOracle() {
        try  {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "SALIZACAFE";
            String password = "system";

            conn = DriverManager.getConnection(dbURL,user,password);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if(conn == null || conn.isClosed()) {
                if(currentDb.equals("oracle")) {
                    initOracle();
                } else {
                    // init postgres
                    initPostgres();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null || !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
