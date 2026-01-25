package de.imnotpan.orderinventory.domain.inventory.exception;

public class InsufficentStockException extends RuntimeException {
    public InsufficentStockException(Long productId, long requested, long available){
        super("Insufficent stock for product id " + productId +  " (requested=" + requested + ", available=" + available + ")");
    }
}
