package com.ilimitech.delivery.spring.paymentmethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {}

