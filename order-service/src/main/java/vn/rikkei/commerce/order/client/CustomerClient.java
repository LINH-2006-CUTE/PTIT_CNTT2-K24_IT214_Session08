package vn.rikkei.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.rikkei.commerce.order.dto.CustomerSnapshot;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/{id}")
    CustomerSnapshot findById(@PathVariable("id") Long id);
}

