package com.ilimitech.delivery.spring.referralcodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Long> {}

