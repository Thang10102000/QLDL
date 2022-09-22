package com.neo.vas.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataSourceConnection {
    //    db NEO
//    private static final String DB_URL = "jdbc:oracle:thin:@10.252.10.221:1521:mpay";
//    private static final String USERNAME = "vasonline";
//    private static final String PASSWORD = "vasonline2021";

//        db Mobifone
    private static final String DB_URL = "jdbc:oracle:thin:@//10.54.5.218:1521/vasol";
    private static final String USERNAME = "vasol_2021";
    private static final String PASSWORD = "vasol2021";

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl(DB_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
//        config.addDataSourceProperty("minimumIdle", "5");
//        config.addDataSourceProperty("maximumPoolSize", "40");
        ds = new HikariDataSource(config);
    }

    public DataSourceConnection() {
    }
    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
}
