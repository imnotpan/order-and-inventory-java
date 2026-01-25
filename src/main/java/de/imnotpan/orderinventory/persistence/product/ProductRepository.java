package de.imnotpan.orderinventory.persistence.product;

import de.imnotpan.orderinventory.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);

    @Query("select p.id from Product p where p.sku = :sku")
    Optional<Long> findIdBySku(@Param("sku") String sku);

}