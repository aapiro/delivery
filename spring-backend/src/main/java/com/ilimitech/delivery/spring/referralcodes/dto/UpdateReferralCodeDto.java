package com.ilimitech.delivery.spring.referralcodes.dto;
import java.math.BigDecimal;
public class UpdateReferralCodeDto {
    private String code;
    private BigDecimal discountAmount;
    private Integer timesUsed;
    private Integer maxUses;
    public UpdateReferralCodeDto() {}
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal v) { this.discountAmount = v; }
    public Integer getTimesUsed() { return timesUsed; } public void setTimesUsed(Integer v) { this.timesUsed = v; }
    public Integer getMaxUses() { return maxUses; } public void setMaxUses(Integer v) { this.maxUses = v; }
}

