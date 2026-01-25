package de.imnotpan.orderinventory.domain.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id){
        super("Product with id " + id + " not found.");
    }
    public ProductNotFoundException(String sku){
        super("Product with sku " + sku + " not found.");
    }
}
