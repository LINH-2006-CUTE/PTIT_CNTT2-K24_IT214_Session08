package vn.rikkei.commerce.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.rikkei.commerce.inventory.dto.ReservationResponse;
import vn.rikkei.commerce.inventory.dto.ReserveRequest;
import vn.rikkei.commerce.inventory.dto.StockUpdateRequest;
import vn.rikkei.commerce.inventory.model.Inventory;
import vn.rikkei.commerce.inventory.repository.InventoryRepository;
import vn.rikkei.commerce.inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryRepository repository;
    private final InventoryService service;

    @GetMapping
    public List<Inventory> findAll() {
        return repository.findAll();
    }

    @GetMapping("/product/{productId}")
    public Inventory findByProduct(@PathVariable Long productId) {
        return service.findByProductId(productId);
    }

    @PutMapping("/product/{productId}")
    public Inventory setStock(@PathVariable Long productId,
                              @Valid @RequestBody StockUpdateRequest request) {
        return service.setStock(productId, request.quantity());
    }

    @PostMapping("/reserve")
    public ReservationResponse reserve(@Valid @RequestBody ReserveRequest request) {
        return service.reserve(request.productId(), request.quantity());
    }
}

