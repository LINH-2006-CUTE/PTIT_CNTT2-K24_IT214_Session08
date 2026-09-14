package vn.rikkei.commerce.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePaymentRequest(
        @NotNull Long orderId,
        @NotBlank @Pattern(regexp = "CARD|BANK_TRANSFER|COD",
                message = "method phải là CARD, BANK_TRANSFER hoặc COD") String method
) {}

