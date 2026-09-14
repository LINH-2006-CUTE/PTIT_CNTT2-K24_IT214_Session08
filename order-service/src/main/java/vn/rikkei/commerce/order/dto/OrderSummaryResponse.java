package vn.rikkei.commerce.order.dto;

import vn.rikkei.commerce.order.model.Order;
import vn.rikkei.commerce.order.model.OrderItem;
import java.util.List;

public record OrderSummaryResponse(
        Order order,
        CustomerSnapshot customer,
        List<OrderLineSummary> lines,
        PaymentSnapshot payment
) {
    public record OrderLineSummary(OrderItem orderedItem, ProductSnapshot product,
                                   InventorySnapshot currentInventory) {}
}

