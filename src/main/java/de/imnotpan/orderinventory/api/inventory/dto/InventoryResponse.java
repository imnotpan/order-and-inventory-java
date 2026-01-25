package de.imnotpan.orderinventory.api.inventory.dto;

public record InventoryResponse (
    String sku,
    long availableQuantity,
    long reservedQuantity
){}
