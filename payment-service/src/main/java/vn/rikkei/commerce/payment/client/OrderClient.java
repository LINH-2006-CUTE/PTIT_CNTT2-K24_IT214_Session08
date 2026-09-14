package vn.rikkei.commerce.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.rikkei.commerce.payment.dto.OrderSnapshot;

@FeignClient(name = "order-service")
public interface OrderClient {
    @GetMapping("/api/v1/orders/{id}")
    OrderSnapshot findById(@PathVariable("id") Long id);

    @PutMapping("/api/v1/orders/{id}/paid")
    OrderSnapshot markPaid(@PathVariable("id") Long id);
}

