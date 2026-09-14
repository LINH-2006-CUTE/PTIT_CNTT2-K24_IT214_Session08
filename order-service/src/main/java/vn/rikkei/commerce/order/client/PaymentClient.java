package vn.rikkei.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.rikkei.commerce.order.dto.PaymentSnapshot;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @GetMapping("/api/v1/payments/order/{orderId}")
    PaymentSnapshot findByOrderId(@PathVariable("orderId") Long orderId);
}

