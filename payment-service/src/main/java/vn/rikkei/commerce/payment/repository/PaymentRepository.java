package vn.rikkei.commerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.rikkei.commerce.payment.model.Payment;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}

