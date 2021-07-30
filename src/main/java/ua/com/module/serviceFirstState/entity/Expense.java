package ua.com.module.serviceFirstState.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("expense")
public class Expense extends Operation {

    public Expense(Account account, Double sum, String description, ExpenseType expense) {
        super(account, sum, description);
        this.expense = expense;
    }

    public Expense() {
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('BILL', 'CREDIT')")
    private Expense.ExpenseType expense;

    public enum ExpenseType {
        BILL, CREDIT
    }

    public ExpenseType getExpense() {
        return expense;
    }

    public void setExpense(ExpenseType expense) {
        this.expense = expense;
    }

}
