package com.ilimitech.delivery.spring.referralcodes.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public class CreateReferralCodeDto {
    @NotNull private Long userId;
    @NotBlank private String code;
    private BigDecimal discountAmount; private Integer maxUses;
    public CreateReferralCodeDto() {}
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal v) { this.discountAmount = v; }
    public Integer getMaxUses() { return maxUses; } public void setMaxUses(Integer v) { this.maxUses = v; }
}

