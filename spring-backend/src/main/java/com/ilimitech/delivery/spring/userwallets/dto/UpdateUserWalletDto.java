package com.ilimitech.delivery.spring.userwallets.dto;

import java.math.BigDecimal;

public class UpdateUserWalletDto {
    private BigDecimal balance;
    private String currency;

    public UpdateUserWalletDto() {}
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

