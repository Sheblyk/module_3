package ua.com.module.serviceFirstState.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    public Account(Client client, Double sum) {
        this.client = client;
        this.sum = sum;
    }

    public Account(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Operation> operations = new ArrayList<>();

    private Double sum;

    public Long getAccount_id() {
        return account_id;
    }

    public void setClient_id(Long account_id) {
        this.account_id = account_id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        client.addAccount(this);
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void addOperation(Operation operation){
        operations.add(operation);
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", sum=" + sum +
                '}';
    }
}
