package ua.com.module.serviceSecondState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnect {

    private static final Logger loggerInfo = LoggerFactory.getLogger("info");
    private static final Logger loggerWarn = LoggerFactory.getLogger("warn");
    private static final Logger loggerError = LoggerFactory.getLogger("error");

    public Connection getDbConnect(String root, String password) {
        loggerInfo.info("Start getting url from properties");
        Properties properties = loadProperties();
        String url = properties.getProperty("url");
        try {
            return DriverManager.getConnection(url, root, password);
        } catch (SQLException e) {
            loggerError.error("Can`t upload url from properties, connection doesn`t created");
            System.out.println("Connection wasn`t created");
            return null;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DBConnect.class.getResourceAsStream("/connection.properties")) {
            properties.load(input);
            loggerInfo.info("Loaded properties");
        } catch (IOException e) {
            System.out.println("Couldn`t load Properties");
        }
        return properties;
    }
}
