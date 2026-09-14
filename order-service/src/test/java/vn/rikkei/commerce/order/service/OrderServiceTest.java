package vn.rikkei.commerce.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.rikkei.commerce.order.client.*;
import vn.rikkei.commerce.order.dto.*;
import vn.rikkei.commerce.order.model.Order;
import vn.rikkei.commerce.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository repository;
    @Mock CustomerClient customerClient;
    @Mock ProductClient productClient;
    @Mock InventoryClient inventoryClient;
    @Mock PaymentClient paymentClient;
    @InjectMocks OrderService service;

    @Test
    void createAggregatesDuplicateLinesAndCalculatesTotal() {
        when(customerClient.findById(1L)).thenReturn(
                new CustomerSnapshot(1L, "Test", "test@example.com", null, null));
        when(productClient.findById(10L)).thenReturn(
                new ProductSnapshot(10L, "Mouse", "", new BigDecimal("100.00"), "ACCESSORY", true));
        when(inventoryClient.findByProductId(10L)).thenReturn(
                new InventorySnapshot(1L, 10L, 20));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.create(new CreateOrderRequest(1L, List.of(
                new CreateOrderRequest.OrderItemRequest(10L, 1),
                new CreateOrderRequest.OrderItemRequest(10L, 2))));

        assertEquals(1, result.getItems().size());
        assertEquals(3, result.getItems().getFirst().getQuantity());
        assertEquals(new BigDecimal("300.00"), result.getTotalAmount());
        verify(inventoryClient).reserve(new ReserveRequest(10L, 3));
    }
}

