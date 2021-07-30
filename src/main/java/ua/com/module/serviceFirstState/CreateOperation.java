package ua.com.module.serviceFirstState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.module.serviceFirstState.entity.*;

import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateOperation {
    private BufferedReader reader;

    private static final Logger loggerInfo = LoggerFactory.getLogger("info");
    private static final Logger loggerWarn = LoggerFactory.getLogger("warn");
    private static final Logger loggerError = LoggerFactory.getLogger("error");

    public void control(String email_) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        Configuration configuration = SetConfig.SetConfig("root", "ghfdsq16");
        configuration.setProperty("hibernate.show_sql", "false");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                Client currentClient = findCurrentClient(session, email_);
                List<Account> accounts = findAllAccounts(session, currentClient);
                for (Account a : accounts) {
                    System.out.println(a);
                }
                Account account = findCurrentAc(session, accounts.size());
                Double sum = setSum(account, session);
                loggerWarn.warn("Start commit Operation");
                session.getTransaction().begin();
                if (sum > 0) {
                    Income income = new Income();
                    income.setSum(sum);
                    income.setAccount(account);
                    income.setIncome(setIncomeCat(session));
                    income.setDescription(setDesc(session));
                    account.setSum(account.getSum() + sum);
                    session.persist(income);
                    session.persist(account);
                } else {
                    Expense expense = new Expense();
                    expense.setSum(sum);
                    expense.setAccount(account);
                    expense.setExpense(setExpansecat(session));
                    expense.setDescription(setDesc(session));
                    account.setSum(account.getSum() + sum);
                    session.persist(expense);
                    session.persist(account);
                }
                session.getTransaction().commit();
                System.out.println("Operation was added");
                loggerInfo.info("End commit operation");
            } catch (Exception e) {
                loggerError.error("Rollback operation");
                session.getTransaction().rollback();
                System.out.println(e.getMessage());
            }
        }
    }

    public String setDesc(Session session) throws IOException {
        loggerInfo.info("Start set description");
        System.out.println("Enter additional info: ");
        loggerInfo.info("End set description");
        return reader.readLine();
    }

    public Income.IncomeType setIncomeCat(Session session) throws IOException {
        System.out.println("It is income operation: choose category");
        System.out.println("1 -> " + Income.IncomeType.SALARY);
        System.out.println("2 -> " + Income.IncomeType.SCOLARSHIP);
        loggerInfo.info("Start set income category");
        int a = Integer.parseInt(reader.readLine());
        switch (a) {
            case 1: {
                loggerInfo.info("End set income category " + Income.IncomeType.SALARY);
                return Income.IncomeType.SALARY;
            }
            case 2: {
                loggerInfo.info("End set income category " + Income.IncomeType.SCOLARSHIP);
                return Income.IncomeType.SCOLARSHIP;
            }
            default: {
                loggerError.error("Entered choise doesn`t exist - can`t set income category");
                throw new IllegalArgumentException("Sorry, this category does not exist");
            }
        }
    }

    public Expense.ExpenseType setExpansecat(Session session) throws IOException {
        System.out.println("It is expanse operation: choose category");
        System.out.println("1 -> " + Expense.ExpenseType.BILL);
        System.out.println("2 -> " + Expense.ExpenseType.CREDIT);
        loggerInfo.info("Start set expanse category");
        int a = Integer.parseInt(reader.readLine());
        switch (a) {
            case 1: {
                loggerInfo.info("End set expanse category " + Expense.ExpenseType.BILL);
                return Expense.ExpenseType.BILL;
            }
            case 2: {
                loggerInfo.info("End set expanse category " + Expense.ExpenseType.CREDIT);
                return Expense.ExpenseType.CREDIT;
            }
            default: {
                loggerError.error("Entered choise doesn`t exist - can`t set expense category");
                throw new IllegalArgumentException("Sorry, this category does not exist");
            }
        }
    }

    public Double setSum(Account account, Session session) throws IOException {
        System.out.println("Please, enter the sum\n(if it is expense operation, enter '-' before the sum)");
        double currentSum = Double.parseDouble(reader.readLine());
        loggerInfo.info("Start set sum of operation");
        if (currentSum == 0) {
            loggerError.error("Sum equals 0");
            throw new IllegalArgumentException("Operation sum can not be 0");
        }
        if (currentSum < 0 && Math.abs(currentSum) >= account.getSum()) {
            loggerError.error("Account hasn`t enough sum for this operation");
            throw new IllegalArgumentException("This sum is more than sum on current account");
        }
        return currentSum;
    }

    public Account findCurrentAc(Session session, int size) throws IOException {
        System.out.println("\nPlease, choose the account - enter id");
        loggerInfo.info("Start upload account for operation");
        long currentAcId = Long.parseLong(reader.readLine());
        if (currentAcId > size) {
            loggerError.error("Account doesn`t exist");
            throw new IllegalArgumentException("This account doesn`t exist");
        }
        loggerInfo.info("End upload account for operation");
        return session.find(Account.class, currentAcId);
    }

    public Client findCurrentClient(Session session, String email_) {
        loggerInfo.info("Start upload client by email" + email_);
        Query query = session.createQuery("from Client cl where cl.email = :email_");
        query.setMaxResults(1);
        query.setParameter("email_", email_);
        Client current = (Client) query.getSingleResult();
        if (current != null) {
            System.out.println("Hi, " + current.getName() + " " +
                    current.getSurname() + "! Choose you account: ");
            loggerInfo.info("End upload client by email " + email_);
            return current;
        }
        loggerError.error("Can`t upload client by email" + email_);
        throw new RuntimeException("Client doesn`t exist");
    }

    public List<Account> findAllAccounts(Session session, Client client) {
        loggerInfo.info("Start upload accounts by client id " + client.getClient_id());
        Query query = session.createQuery("from Account ac where ac.client.client_id = :clId");
        query.setParameter("clId", client.getClient_id());
        List<Account> accounts = (List<Account>) query.getResultList();
        if (accounts.size() != 0) {
            loggerInfo.info("End upload accounts by client id " + client.getClient_id());
            return accounts;
        }
        loggerError.error("Client with id " + client.getClient_id() + " has no acccounts");
        throw new RuntimeException("This client has no accounts");
    }
}
