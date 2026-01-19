package de.imnotpan.orderinventory.api.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        boolean active
){}
