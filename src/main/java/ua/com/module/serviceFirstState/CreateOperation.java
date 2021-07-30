package ua.com.module.serviceFirstState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.com.module.serviceFirstState.entity.*;

import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateOperation {
    private BufferedReader reader;

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
                session.getTransaction().begin();
                if (sum > 0) {
                    Income income = new Income();
                    income.setSum(sum);
                    income.setAccount(account);
                    income.setIncome(setIncomeCat(session));
                    income.setDescription(setDesc(session));
                    session.persist(income);
                } else {
                    Expense expense = new Expense();
                    expense.setSum(sum);
                    expense.setAccount(account);
                    expense.setExpense(setExpansecat(session));
                    expense.setDescription(setDesc(session));
                    session.persist(expense);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println(e.getMessage());
            }
        }
    }

    public String setDesc(Session session) throws IOException {
        System.out.println("Enter additional info: ");
        return reader.readLine();
    }

    public Income.IncomeType setIncomeCat(Session session) throws IOException {
        System.out.println("It is income operation: choose category");
        System.out.println("1 -> " + Income.IncomeType.SALARY);
        System.out.println("2 -> " + Income.IncomeType.SCOLARSHIP);
        int a = Integer.parseInt(reader.readLine());
        switch (a) {
            case 1: {
                return Income.IncomeType.SALARY;
            }
            case 2: {
                return Income.IncomeType.SCOLARSHIP;
            }
            default: {
                throw new IllegalArgumentException("Sorry, this category does not exist");
            }
        }
    }

    public Expense.ExpenseType setExpansecat(Session session) throws IOException {
        System.out.println("It is expanse operation: choose category");
        System.out.println("1 -> " + Expense.ExpenseType.BILL);
        System.out.println("2 -> " + Expense.ExpenseType.CREDIT);
        int a = Integer.parseInt(reader.readLine());
        switch (a) {
            case 1: {
                return Expense.ExpenseType.BILL;
            }
            case 2: {
                return Expense.ExpenseType.CREDIT;
            }
            default: {
                throw new IllegalArgumentException("Sorry, this category does not exist");
            }
        }
    }

    public Double setSum(Account account, Session session) throws IOException {
        System.out.println("Please, enter the sum\n(if it is expense operation, enter '-' before the sum)");
        double currentSum = Double.parseDouble(reader.readLine());
        if (currentSum == 0) {
            throw new IllegalArgumentException("Operation sum can not be 0");
        }
        if (currentSum < 0 && Math.abs(currentSum) >= account.getSum()) {
            throw new IllegalArgumentException("This sum is more than sum on current account");
        }
        return currentSum;
    }

    public Account findCurrentAc(Session session, int size) throws IOException {
        System.out.println("\nPlease, choose the account - enter id");
        long currentAcId = Long.parseLong(reader.readLine());
        if (currentAcId > size){
            throw new IllegalArgumentException("This account doesn`t exist");
        }
        return session.find(Account.class, currentAcId);
    }

    public Client findCurrentClient(Session session, String email_) {
        Query query = session.createQuery("from Client cl where cl.email = :email_");
        query.setMaxResults(1);
        query.setParameter("email_", email_);
        Client current = (Client) query.getSingleResult();
        if (current != null) {
            System.out.println("Hi, " + current.getName() + " " +
                    current.getSurname() + "! Choose you account: ");
            return current;
        }
        throw new RuntimeException("Client doesn`t exist");
    }

    public List<Account> findAllAccounts(Session session, Client client) {
        Query query = session.createQuery("from Account ac where ac.client.client_id = :clId");
        query.setParameter("clId", client.getClient_id());
        List<Account> accounts = (List<Account>) query.getResultList();
        if (accounts.size() != 0) {
            return accounts;
        }
        throw new RuntimeException("This client has no accounts");
    }
}
