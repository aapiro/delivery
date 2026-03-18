package com.ilimitech.delivery.spring.orderissues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderIssueRepository extends JpaRepository<OrderIssue, Long> {}

