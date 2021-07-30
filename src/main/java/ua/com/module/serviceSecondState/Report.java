package ua.com.module.serviceSecondState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.module.serviceFirstState.entity.Account;
import ua.com.module.serviceFirstState.entity.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Report {
    private final static String BY_EMAIl = "SELECT * FROM client WHERE email = ?";
    private final static String ALL_ACCOUNTS = "SELECT * FROM account WHERE client_id = ?";
    private final static String All_OPERATIONS_BY_AC = "SELECT * FROM operation WHERE account_id = ? AND operation.time between ? and ?";
    private final static String DELIMITER = ",";
    private BufferedReader reader;

    private static final Logger loggerInfo = LoggerFactory.getLogger("info");
    private static final Logger loggerWarn = LoggerFactory.getLogger("warn");
    private static final Logger loggerError = LoggerFactory.getLogger("error");

    public void init(String root, String password, String email) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        DBConnect dbConnect = new DBConnect();
        try (Connection connection = dbConnect.getDbConnect(root, password)) {
            Client client = findIdByEmail(connection, email);
            System.out.println(client.getName() + " " +
                    client.getSurname() + "! Choose your account ! ");
            List<Account> accountList = findAllAcPerId(connection, client.getClient_id());
            for (Account a : accountList) {
                System.out.println(a);
            }
            System.out.println("Enter id of account in order to make report");
            String count = reader.readLine();
            if (Long.parseLong(count) <= accountList.size()) {
                System.out.println("Enter date from (format yyyy/M/d)");
                Timestamp from = readDate();
                List<String[]> operations = getOperationByAc(Long.parseLong(count), connection, from, Timestamp.from(Instant.now()));
                if (operations.size() != 0) {
                    writeToFile(operations);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Timestamp readDate(){
        DateFormat format = new SimpleDateFormat("yyyy/M/d");
        loggerInfo.info("Start to set Date from ");
        while(true){
           try{
               Date date = format.parse(reader.readLine());
               return new Timestamp(date.getTime());
           }
           catch (IOException | ParseException e) {
               loggerError.error("Invalid Date input");
                System.out.println("Invalid input, enter once more");
            }
        }
    }

    private List<Account> findAllAcPerId(Connection connection, Long Id) {
        loggerInfo.info("Start getting all accounts by client id " + Id);
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement pr = connection.prepareStatement(ALL_ACCOUNTS)) {
            pr.setLong(1, Id);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccount_id(resultSet.getLong("account_id"));
                account.setSum(resultSet.getDouble("sum"));
                accounts.add(account);
            }
            loggerInfo.info("End getting all accounts by client id " + Id);
            return accounts;
        } catch (SQLException e) {
            loggerError.error("Can`t upload acounts by client id " + Id);
            System.out.println(e.getMessage());
        }
        throw new RuntimeException();
    }

    private Client findIdByEmail(Connection connection, String email) {
        loggerInfo.info("Start upload client by email " + email);
        try (PreparedStatement pr = connection.prepareStatement(BY_EMAIl)) {
            pr.setString(1, email);
            ResultSet resultSet = pr.executeQuery();
            resultSet.next();
            Client client = new Client();
            client.setClient_id(resultSet.getLong("client_id"));
            client.setEmail(email);
            client.setName(resultSet.getString("name"));
            client.setSurname(resultSet.getString("surname"));
            loggerInfo.info("Found client by email" + email);
            return client;
        } catch (SQLException e) {
            loggerError.error("Can`t find client by email ");
            System.out.println("Couldn`t find client by email " + email);
        }
        throw new RuntimeException();
    }

    private List<String[]> getOperationByAc(Long IdAcc, Connection connection, Timestamp from, Timestamp to) {
        loggerInfo.info("Start upload all operations by acount id" + IdAcc);
        List<String[]> lines = new ArrayList<>();
        try (PreparedStatement pr = connection.prepareStatement(All_OPERATIONS_BY_AC)) {
            pr.setLong(1, IdAcc);
            pr.setTimestamp(2, from);
            pr.setTimestamp(3, to);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()) {
                String[] line = new String[6];
                String res;
                if (resultSet.getObject("income") != null) {
                    res = resultSet.getObject("income").toString();
                } else {
                    res = resultSet.getObject("expense").toString();
                }
                line[0] = resultSet.getString("operation_id");
                line[1] = resultSet.getString("sum");
                line[2] = resultSet.getTimestamp("time").toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                line[3] = resultSet.getString("operation_type");
                line[4] = res;
                line[5] = resultSet.getString("description");
                lines.add(line);
            }
            loggerInfo.info("End upload operations by account id " + IdAcc);
            return lines;
        } catch (SQLException e) {
            loggerError.error("Can`t upload operations by account id " + IdAcc);
        }
        throw new RuntimeException();
    }

    public void writeToFile(List<String[]> operations) {
        loggerWarn.warn("Start writing to file report.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.csv", false))) {
            writer.write("operationId, sum, time, commonType, subType, description\n");
            for (String[] o : operations) {
                String line = o[0] + DELIMITER +
                        o[1] + DELIMITER + o[2] + DELIMITER +
                        o[3] + DELIMITER + o[4].toLowerCase() + DELIMITER +
                        o[5] + "\n";
                writer.write(line);
            }
            loggerInfo.info("End writing to file report.csv");
        } catch (IOException e) {
            loggerError.error("Can`t write operations to file");
            System.out.println(e.getMessage());
        }
        System.out.println("Success! Open report.csv to see your report \n");
    }
}

