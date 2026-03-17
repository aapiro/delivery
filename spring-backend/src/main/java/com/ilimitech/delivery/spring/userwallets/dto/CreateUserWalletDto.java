package com.ilimitech.delivery.spring.userwallets.dto;

import java.math.BigDecimal;

public class CreateUserWalletDto {
    private Long userId;
    private BigDecimal balance;
    private String currency;

    public CreateUserWalletDto() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

