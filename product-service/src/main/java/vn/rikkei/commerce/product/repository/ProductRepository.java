package vn.rikkei.commerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.rikkei.commerce.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}

