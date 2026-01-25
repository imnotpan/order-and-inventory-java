package de.imnotpan.orderinventory.domain.inventory.exception;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(Long productId){
        super("Inventory for product id " + productId + " not found");
    }
}
