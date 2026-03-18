package com.ilimitech.delivery.spring.payouts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {}

