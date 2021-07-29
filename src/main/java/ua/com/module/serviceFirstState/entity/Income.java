package ua.com.module.serviceFirstState.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("income")
public class Income extends Operation{

    public Income(Account account, Double sum, String description, IncomeType income) {
        super(account, sum, description);
        this.income = income;
    }

    public Income(){}

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('SALARY', 'SCOLARSHIP')")
    private IncomeType income;

    public enum IncomeType{
        SALARY, SCOLARSHIP
    }

    public IncomeType getIncome() {
        return income;
    }

    public void setIncome(IncomeType income) {
        this.income = income;
    }
}
