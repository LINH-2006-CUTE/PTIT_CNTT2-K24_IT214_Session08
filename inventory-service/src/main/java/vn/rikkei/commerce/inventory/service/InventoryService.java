package vn.rikkei.commerce.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.rikkei.commerce.inventory.dto.ReservationResponse;
import vn.rikkei.commerce.inventory.model.Inventory;
import vn.rikkei.commerce.inventory.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    public Inventory findByProductId(Long productId) {
        return repository.findByProductId(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có tồn kho cho sản phẩm " + productId));
    }

    @Transactional
    public Inventory setStock(Long productId, int quantity) {
        Inventory inventory = repository.findByProductIdForUpdate(productId)
                .orElseGet(() -> Inventory.builder().productId(productId).build());
        inventory.setAvailableQuantity(quantity);
        return repository.save(inventory);
    }

    @Transactional
    public ReservationResponse reserve(Long productId, int quantity) {
        Inventory inventory = repository.findByProductIdForUpdate(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có tồn kho cho sản phẩm " + productId));
        if (inventory.getAvailableQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Không đủ hàng. Còn " + inventory.getAvailableQuantity() + ", yêu cầu " + quantity);
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        repository.save(inventory);
        return new ReservationResponse(productId, quantity, inventory.getAvailableQuantity(), "Giữ hàng thành công");
    }
}

