package com.hotel.booking.repository;
import com.hotel.booking.model.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment , Long> {

    @Query("""
        SELECT p
        FROM Payment p
        WHERE p.paymentIntentID = :paymentIntentId
    """)
    Payment findBypaymentIntentId(@Param("paymentIntentId") String paymentIntentId);

    //Modifies data so must annotate with @Modifying
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Payment p
        WHERE p.booking.user.email = 'e2e@test.com'
    """)
    void deleteE2EPayments();
}
