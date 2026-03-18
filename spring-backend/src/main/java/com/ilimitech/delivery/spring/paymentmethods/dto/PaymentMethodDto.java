package com.ilimitech.delivery.spring.paymentmethods.dto;
public class PaymentMethodDto {
    private Long id; private Long userId; private String type; private String provider;
    private String token; private String lastFour; private Boolean isDefault; private Boolean isActive;
    public PaymentMethodDto() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
    public String getProvider() { return provider; } public void setProvider(String v) { this.provider = v; }
    public String getToken() { return token; } public void setToken(String v) { this.token = v; }
    public String getLastFour() { return lastFour; } public void setLastFour(String v) { this.lastFour = v; }
    public Boolean getIsDefault() { return isDefault; } public void setIsDefault(Boolean v) { this.isDefault = v; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
}

