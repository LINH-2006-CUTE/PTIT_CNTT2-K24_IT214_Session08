package vn.rikkei.commerce.payment.dto;

import java.math.BigDecimal;

public record OrderSnapshot(Long id, Long customerId, String status, BigDecimal totalAmount) {}

