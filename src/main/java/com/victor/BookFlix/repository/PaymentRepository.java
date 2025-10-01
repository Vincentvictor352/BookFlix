package com.victor.BookFlix.repository;

import com.victor.BookFlix.entity.Payment;
import com.victor.BookFlix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser(User user);
    Payment findByReference(String reference);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    double sumSuccessfulPayments();

}
