package ua.com.module.serviceFirstState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.com.module.serviceFirstState.entity.Account;
import ua.com.module.serviceFirstState.entity.Client;
import ua.com.module.serviceFirstState.entity.Expense;
import ua.com.module.serviceFirstState.entity.Income;

public class FillDB {

    public FillDB() {
      Configuration configuration = SetConfig.SetConfig("root", "ghfdsq16");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
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
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
