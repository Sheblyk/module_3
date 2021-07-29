package ua.com.module.serviceFirstState;

import org.hibernate.cfg.Configuration;

public class SetConfig {

    public static Configuration SetConfig(String root, String password) {
        Configuration configuration = new Configuration().configure();
        configuration.setProperty("hibernate.connection.username", root);
        configuration.setProperty("hibernate.connection.password", password);
        return configuration;
    }
}
