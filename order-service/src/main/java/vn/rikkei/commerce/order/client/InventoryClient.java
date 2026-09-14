package vn.rikkei.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.rikkei.commerce.order.dto.InventorySnapshot;
import vn.rikkei.commerce.order.dto.ReserveRequest;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/v1/inventories/product/{productId}")
    InventorySnapshot findByProductId(@PathVariable("productId") Long productId);

    @PostMapping("/api/v1/inventories/reserve")
    Object reserve(@RequestBody ReserveRequest request);
}

