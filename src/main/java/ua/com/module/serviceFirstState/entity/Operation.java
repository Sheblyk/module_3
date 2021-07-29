package ua.com.module.serviceFirstState.entity;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "operation")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "operation_type")
public abstract class Operation {

    public Operation(Account account, Double sum, String description) {
        this.account = account;
        this.sum = sum;
        this.description = description;
        this.time = Instant.now();
    }

    public Operation(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operation_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private Double sum;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant time;

    @Column
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        account.addOperation(this);
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

}
