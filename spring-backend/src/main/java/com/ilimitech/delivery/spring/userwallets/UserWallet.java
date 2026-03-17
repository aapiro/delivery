package com.ilimitech.delivery.spring.userwallets;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_wallets")
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    private String currency;

    public UserWallet() {}

    public UserWallet(Long id, Long userId, BigDecimal balance, String currency) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private BigDecimal balance;
        private String currency;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder userId(Long userId){ this.userId = userId; return this; }
        public Builder balance(BigDecimal balance){ this.balance = balance; return this; }
        public Builder currency(String currency){ this.currency = currency; return this; }
        public UserWallet build(){ return new UserWallet(id,userId,balance,currency); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

