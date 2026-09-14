package vn.rikkei.commerce.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.rikkei.commerce.payment.dto.CreatePaymentRequest;
import vn.rikkei.commerce.payment.dto.PaymentResponse;
import vn.rikkei.commerce.payment.repository.PaymentRepository;
import vn.rikkei.commerce.payment.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository repository;
    private final PaymentService service;

    @GetMapping
    public List<PaymentResponse> findAll() {
        return repository.findAll().stream().map(PaymentResponse::from).toList();
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse findByOrder(@PathVariable Long orderId) {
        return service.findByOrderId(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest request) {
        return service.create(request);
    }
}

