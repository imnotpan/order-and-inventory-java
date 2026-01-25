package de.imnotpan.orderinventory.domain.product.exception;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(String sku) {
        super("Product with Sku '" + sku + "' already exists");
    }
}
