package com.ilimitech.delivery.spring.referralcodes;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name = "referral_codes")
public class ReferralCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long userId;
    private String code;
    @Column(precision = 10, scale = 2) private BigDecimal discountAmount;
    private Integer timesUsed;
    private Integer maxUses;
    public ReferralCode() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal v) { this.discountAmount = v; }
    public Integer getTimesUsed() { return timesUsed; } public void setTimesUsed(Integer v) { this.timesUsed = v; }
    public Integer getMaxUses() { return maxUses; } public void setMaxUses(Integer v) { this.maxUses = v; }
}

