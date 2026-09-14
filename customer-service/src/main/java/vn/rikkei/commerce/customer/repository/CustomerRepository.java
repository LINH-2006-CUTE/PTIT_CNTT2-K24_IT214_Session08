package vn.rikkei.commerce.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.rikkei.commerce.customer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}

