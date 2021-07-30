package ua.com.module.serviceSecondState;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnect {
    public Connection getDbConnect(String root, String password) {
        Properties properties = loadProperties();
        String url = properties.getProperty("url");
        try {
            return DriverManager.getConnection(url, root, password);
        } catch (SQLException e) {
            System.out.println("Connection wasn`t created");
            return null;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DBConnect.class.getResourceAsStream("/connection.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Couldn`t load Properties");
        }
        return properties;
    }
}
