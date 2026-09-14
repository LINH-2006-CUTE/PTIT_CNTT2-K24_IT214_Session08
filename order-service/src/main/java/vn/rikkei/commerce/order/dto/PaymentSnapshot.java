package vn.rikkei.commerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSnapshot(Long id, Long orderId, BigDecimal amount, String method,
                              String status, LocalDateTime paidAt) {}

