package vn.rikkei.commerce.inventory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import vn.rikkei.commerce.inventory.dto.ReservationResponse;
import vn.rikkei.commerce.inventory.model.Inventory;
import vn.rikkei.commerce.inventory.repository.InventoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock InventoryRepository repository;
    @InjectMocks InventoryService service;

    @Test
    void reserveDecreasesAvailableQuantity() {
        Inventory stock = Inventory.builder().productId(1L).availableQuantity(5).build();
        when(repository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(stock));

        ReservationResponse response = service.reserve(1L, 2);

        assertEquals(3, response.remainingQuantity());
        assertEquals(3, stock.getAvailableQuantity());
        verify(repository).save(stock);
    }

    @Test
    void reserveRejectsInsufficientStock() {
        Inventory stock = Inventory.builder().productId(1L).availableQuantity(1).build();
        when(repository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(stock));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.reserve(1L, 2));

        assertEquals(409, error.getStatusCode().value());
        verify(repository, never()).save(any());
    }
}

