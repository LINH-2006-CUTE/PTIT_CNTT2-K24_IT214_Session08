package vn.rikkei.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.rikkei.commerce.payment.client.OrderClient;
import vn.rikkei.commerce.payment.dto.CreatePaymentRequest;
import vn.rikkei.commerce.payment.dto.OrderSnapshot;
import vn.rikkei.commerce.payment.dto.PaymentResponse;
import vn.rikkei.commerce.payment.model.Payment;
import vn.rikkei.commerce.payment.repository.PaymentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final OrderClient orderClient;

    public PaymentResponse findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .map(PaymentResponse::from)
                .orElseGet(() -> PaymentResponse.notPaid(orderId));
    }

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {
        if (repository.existsByOrderId(request.orderId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đơn hàng đã được thanh toán");
        }
        OrderSnapshot order = orderClient.findById(request.orderId());
        if ("CANCELLED".equals(order.status())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Không thể thanh toán đơn đã huỷ");
        }
        Payment payment = repository.save(Payment.builder()
                .orderId(order.id()).amount(order.totalAmount())
                .method(request.method()).status("PAID").paidAt(LocalDateTime.now()).build());
        orderClient.markPaid(order.id());
        return PaymentResponse.from(payment);
    }
}

