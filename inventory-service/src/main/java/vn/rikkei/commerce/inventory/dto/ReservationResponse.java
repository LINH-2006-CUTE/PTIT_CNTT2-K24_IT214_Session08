package vn.rikkei.commerce.inventory.dto;

public record ReservationResponse(
        Long productId,
        Integer reservedQuantity,
        Integer remainingQuantity,
        String message
) {}

