package ua.com.module.serviceFirstState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.module.serviceFirstState.entity.Account;
import ua.com.module.serviceFirstState.entity.Client;
import ua.com.module.serviceFirstState.entity.Expense;
import ua.com.module.serviceFirstState.entity.Income;

public class FillDB {

    private static final Logger loggerInfo = LoggerFactory.getLogger("info");
    private static final Logger loggerWarn = LoggerFactory.getLogger("warn");
    private static final Logger loggerError = LoggerFactory.getLogger("error");

    public FillDB() {
        loggerWarn.warn("Start get configuration (hibernate)");
      Configuration configuration = SetConfig.SetConfig("root", "ghfdsq16");
      loggerInfo.info("Uploaded configuration (hibernate)");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            loggerWarn.warn("Start input test data into db");
            session.getTransaction().begin();
            Client client = new Client("S", "Daris", "koko@gmail.com");
            session.persist(client);

            Account account = new Account(client, 300.30);
            session.persist(account);
            Account account1 = new Account(client, 1300.30);
            session.persist(account1);

            Income income = new Income(account, 100.00, "for may", Income.IncomeType.SALARY);
            session.persist(income);
            Expense expense = new Expense(account, -10.00, "vodafone", Expense.ExpenseType.BILL);
            session.persist(expense);

            Income income1 = new Income(account1, 1200.00, "award", Income.IncomeType.SALARY);
            session.persist(income1);
            Expense expense1 = new Expense(account1, -300.00, "rent", Expense.ExpenseType.BILL);
            session.persist(expense1);

            session.getTransaction().commit();
            loggerInfo.info("Test data uploaded to DB");
        } catch (Exception e) {
            loggerError.info("Can`t get config; test data wasn`t uploaded");
            System.out.println(e.getMessage());
        }
    }
}
