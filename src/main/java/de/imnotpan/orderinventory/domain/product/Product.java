package de.imnotpan.orderinventory.domain.product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected Product() {}

    public Product(String sku, String name, BigDecimal price){
        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public Long getId() {
        return id;
    }
}
