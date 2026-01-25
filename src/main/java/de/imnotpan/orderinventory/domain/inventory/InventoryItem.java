package de.imnotpan.orderinventory.domain.inventory;

import de.imnotpan.orderinventory.domain.inventory.exception.InsufficentStockException;
import de.imnotpan.orderinventory.domain.inventory.exception.InvalidInventoryOperationException;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryItem {

    @Id
    @Column(name = "product_id")
    private long productId;

    @Column(name = "available_quantity", nullable = false)
    private long availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private long reservedQuantity;

    @Version
    private long version;

    protected InventoryItem() {}
    public InventoryItem(long productId, long initialQuantity) {
        if (initialQuantity < 0){
            throw new IllegalArgumentException("Initial quantity must be >= 0");
        }
        this.productId = productId;
        this.availableQuantity = initialQuantity;
        this.reservedQuantity = 0;
    }

    public void restock(long quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        availableQuantity += quantity;
    }

    public void reserve(long quantity){
        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (availableQuantity < quantity){
            throw new InsufficentStockException(productId, quantity, availableQuantity);
        }
        availableQuantity -= quantity;
        reservedQuantity += quantity;
    }

    public void release(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (reservedQuantity < quantity) {
        throw new InvalidInventoryOperationException( "Cannot release more than reserved stock for product " + productId);
        }
        reservedQuantity -= quantity;
        availableQuantity += quantity;
    }

    public void consume(long quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (reservedQuantity < quantity) {
        throw new InvalidInventoryOperationException( "Cannot consume more than reserved stock for product " + productId);
        }
        reservedQuantity -= quantity;
    }

    public long totalQuantity(){
        return reservedQuantity + availableQuantity;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof InventoryItem that)) return false;
        return productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(productId);
    }

    public boolean canReserve(long quantity){
        return quantity > 0 && availableQuantity >= quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getAvailableQuantity() {
        return availableQuantity;
    }

    public long getReservedQuantity() {
        return reservedQuantity;
    }
}
