package ua.com.module.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.module.serviceFirstState.CreateOperation;
import ua.com.module.serviceFirstState.FillDB;
import ua.com.module.serviceSecondState.Report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Controller {
    private String password;
    private String user;
    private String email;
    public BufferedReader reader;

    private static final Logger loggerInfo = LoggerFactory.getLogger("info");
    private static final Logger loggerWarn = LoggerFactory.getLogger("warn");
    private static final Logger loggerError = LoggerFactory.getLogger("error");

    public Controller() {
        loggerInfo.warn("Start upload local environmets");
        password = System.getenv("PASSWORD");
        user = System.getenv("USER");
        email = System.getenv("EMAIL");
        if (password == null || user == null || email == null) {
            loggerError.error("Can`t upload local environments");
            throw new RuntimeException("Couldn`t load local environment, please set them!");
        }
        loggerInfo.info("Loaded local environments");
    }

    public void run() {
        loggerInfo.info("Open app");
        reader = new BufferedReader(new InputStreamReader(System.in));
        boolean checker = true;
        FillDB fillDB = new FillDB(user,password);
        while (checker) {
            System.out.println("What would you like to do? \n" +
                    "1 -> make operation \n2 -> get report \n3 -> exit\n");
            int result = 0;
            try {
                System.out.println("Enter your chioce");
                result = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            switch (result) {
                case 1: {
                    loggerInfo.info("Start first state");
                    CreateOperation createOperation = new CreateOperation();
                    createOperation.control(email, user, password);
                    loggerInfo.info("End first state");
                    break;
                }
                case 2: {
                    loggerInfo.info("Start second state");
                    Report report = new Report();
                    report.init(user, password, email);
                    loggerInfo.info("End second state");
                    break;
                }
                case 3: {
                    loggerInfo.info("Close app");
                    checker = false;
                    break;
                }
                default: {
                    loggerWarn.warn("User enter invalid choice");
                    System.out.println("Something went wrong - try once more!\n");
                }
            }
        }
    }
}
