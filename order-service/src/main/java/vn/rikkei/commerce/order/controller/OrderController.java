package vn.rikkei.commerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.rikkei.commerce.order.dto.CreateOrderRequest;
import vn.rikkei.commerce.order.dto.OrderSummaryResponse;
import vn.rikkei.commerce.order.model.Order;
import vn.rikkei.commerce.order.repository.OrderRepository;
import vn.rikkei.commerce.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository repository;
    private final OrderService service;

    @GetMapping
    public List<Order> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody CreateOrderRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}/summary")
    public OrderSummaryResponse summary(@PathVariable Long id) {
        return service.summary(id);
    }

    @PutMapping("/{id}/paid")
    public Order markPaid(@PathVariable Long id) {
        return service.markPaid(id);
    }
}

