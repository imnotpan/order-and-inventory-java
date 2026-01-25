package de.imnotpan.orderinventory.application.inventory;

import de.imnotpan.orderinventory.persistence.inventory.InventoryRepository;
import de.imnotpan.orderinventory.domain.inventory.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestockInventoryService {

    private final InventoryRepository repository;

    public RestockInventoryService(InventoryRepository repository){
        this.repository = repository;
    }
    
    @Transactional 
    public void restock(long productId, long quantity){
        InventoryItem item = repository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for productId " + productId));
        item.restock(quantity);
    }
}
