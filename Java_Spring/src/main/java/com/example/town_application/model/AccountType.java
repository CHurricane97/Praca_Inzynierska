package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account_type", schema = "public", catalog = "Town_Database")
public class AccountType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_type_id")
    private int accountTypeId;
    @Basic
    @Column(name = "type")
    private String type;

    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountType that = (AccountType) o;
        return accountTypeId == that.accountTypeId && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountTypeId, type);
    }
}
