package de.imnotpan.orderinventory.persistence.product;

import de.imnotpan.orderinventory.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
}