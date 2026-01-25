package de.imnotpan.orderinventory.domain.inventory.exception;

public class InvalidInventoryOperationException extends RuntimeException {
    public InvalidInventoryOperationException(String message) {
        super(message);
    }
}
