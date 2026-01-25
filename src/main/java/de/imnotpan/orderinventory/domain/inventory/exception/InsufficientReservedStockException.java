package de.imnotpan.orderinventory.domain.inventory.exception;

public class InsufficientReservedStockException extends RuntimeException {
    public InsufficientReservedStockException(long productId, long requested, long reserved) {
        super("Insufficient reserved stock for product " + productId +
              ". Requested: " + requested +
              ", Reserved: " + reserved);
    }
}