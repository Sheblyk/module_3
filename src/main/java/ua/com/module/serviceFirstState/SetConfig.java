package ua.com.module.serviceFirstState;

import org.hibernate.cfg.Configuration;

public class SetConfig {

    public static Configuration SetConfig(String user, String password) {
        Configuration configuration = new Configuration().configure();
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", password);
        return configuration;
    }
}
