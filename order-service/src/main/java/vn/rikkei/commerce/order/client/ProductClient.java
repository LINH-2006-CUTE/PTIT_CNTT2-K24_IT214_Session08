package vn.rikkei.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.rikkei.commerce.order.dto.ProductSnapshot;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    ProductSnapshot findById(@PathVariable("id") Long id);
}

