package vn.rikkei.commerce.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReserveRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
) {}

