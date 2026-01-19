package de.imnotpan.orderinventory.domain.inventory;

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
    public InventoryItem(Long productId, long initialQuantity) {
        this.productId = productId;
        this.availableQuantity = initialQuantity;
        this.reservedQuantity = 0;
    }

    public void reserve(long quantity){
        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (availableQuantity < quantity){
            throw new IllegalStateException("Insufficent stock");
        }
        availableQuantity -= quantity;
        reservedQuantity += quantity;
    }

    public void release(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (availableQuantity < quantity) {
            throw new IllegalStateException("Cannot release more than reserved");
        }
        reservedQuantity -= quantity;
        availableQuantity += quantity;
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
