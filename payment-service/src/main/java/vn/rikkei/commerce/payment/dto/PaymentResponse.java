package vn.rikkei.commerce.payment.dto;

import vn.rikkei.commerce.payment.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, Long orderId, BigDecimal amount, String method,
                              String status, LocalDateTime paidAt) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getAmount(),
                payment.getMethod(), payment.getStatus(), payment.getPaidAt());
    }

    public static PaymentResponse notPaid(Long orderId) {
        return new PaymentResponse(null, orderId, null, null, "NOT_PAID", null);
    }
}

