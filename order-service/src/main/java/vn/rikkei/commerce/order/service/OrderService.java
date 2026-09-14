package vn.rikkei.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.rikkei.commerce.order.client.*;
import vn.rikkei.commerce.order.dto.*;
import vn.rikkei.commerce.order.model.*;
import vn.rikkei.commerce.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    public Order findById(Long id) {
        return repository.findWithItemsById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng " + id));
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        customerClient.findById(request.customerId());

        Map<Long, Integer> requestedQuantities = new LinkedHashMap<>();
        request.items().forEach(item -> requestedQuantities.merge(
                item.productId(), item.quantity(), Integer::sum));

        Map<Long, ProductSnapshot> products = new LinkedHashMap<>();
        requestedQuantities.forEach((productId, quantity) -> {
            ProductSnapshot product = productClient.findById(productId);
            if (!product.active()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Sản phẩm " + productId + " đang ngừng bán");
            }
            InventorySnapshot stock = inventoryClient.findByProductId(productId);
            if (stock.availableQuantity() < quantity) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Sản phẩm " + productId + " chỉ còn " + stock.availableQuantity());
            }
            products.put(productId, product);
        });

        requestedQuantities.forEach((productId, quantity) ->
                inventoryClient.reserve(new ReserveRequest(productId, quantity)));

        Order order = Order.builder()
                .customerId(request.customerId())
                .status(OrderStatus.PENDING_PAYMENT)
                .totalAmount(BigDecimal.ZERO)
                .build();

        requestedQuantities.forEach((productId, quantity) -> {
            ProductSnapshot product = products.get(productId);
            BigDecimal subtotal = product.price().multiply(BigDecimal.valueOf(quantity));
            order.addItem(OrderItem.builder()
                    .productId(productId).productName(product.name())
                    .quantity(quantity).unitPrice(product.price()).subtotal(subtotal).build());
        });
        order.setTotalAmount(order.getItems().stream()
                .map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return repository.save(order);
    }

    public OrderSummaryResponse summary(Long id) {
        Order order = findById(id);
        CustomerSnapshot customer = customerClient.findById(order.getCustomerId());
        List<OrderSummaryResponse.OrderLineSummary> lines = order.getItems().stream()
                .map(item -> new OrderSummaryResponse.OrderLineSummary(
                        item,
                        productClient.findById(item.getProductId()),
                        inventoryClient.findByProductId(item.getProductId())))
                .toList();
        PaymentSnapshot payment = paymentClient.findByOrderId(id);
        return new OrderSummaryResponse(order, customer, lines, payment);
    }

    @Transactional
    public Order markPaid(Long id) {
        Order order = findById(id);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đơn hàng đã bị huỷ");
        }
        order.setStatus(OrderStatus.PAID);
        return repository.save(order);
    }
}

