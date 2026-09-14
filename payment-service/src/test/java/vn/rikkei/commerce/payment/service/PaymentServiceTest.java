package vn.rikkei.commerce.payment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.rikkei.commerce.payment.client.OrderClient;
import vn.rikkei.commerce.payment.dto.CreatePaymentRequest;
import vn.rikkei.commerce.payment.dto.OrderSnapshot;
import vn.rikkei.commerce.payment.dto.PaymentResponse;
import vn.rikkei.commerce.payment.model.Payment;
import vn.rikkei.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock PaymentRepository repository;
    @Mock OrderClient orderClient;
    @InjectMocks PaymentService service;

    @Test
    void createUsesTrustedAmountFromOrderAndMarksItPaid() {
        when(repository.existsByOrderId(7L)).thenReturn(false);
        when(orderClient.findById(7L)).thenReturn(
                new OrderSnapshot(7L, 1L, "PENDING_PAYMENT", new BigDecimal("125000.00")));
        when(repository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setId(99L);
            return payment;
        });

        PaymentResponse response = service.create(new CreatePaymentRequest(7L, "CARD"));

        assertEquals(new BigDecimal("125000.00"), response.amount());
        assertEquals("PAID", response.status());
        verify(orderClient).markPaid(7L);
    }
}

