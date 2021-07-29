package ua.com.module.serviceSecondState.entity;

import java.time.Instant;

public class Operation {
    private Long operation_id;
    private Double sum;
    private Instant time;
    private String operation_type;
    private String subtype;
    private String description;
    private Long account_id;

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operation_id=" + operation_id +
                ", sum=" + sum +
                ", time=" + time +
                ", operation_type='" + operation_type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", description='" + description + '\'' +
                ", account_id=" + account_id +
                '}';
    }
}
