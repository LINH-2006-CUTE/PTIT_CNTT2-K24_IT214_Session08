package vn.rikkei.commerce.order.dto;

import java.math.BigDecimal;

public record ProductSnapshot(Long id, String name, String description, BigDecimal price,
                              String category, boolean active) {}

