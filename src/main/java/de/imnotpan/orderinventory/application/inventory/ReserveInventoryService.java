package de.imnotpan.orderinventory.application.inventory;

import de.imnotpan.orderinventory.persistence.inventory.InventoryRepository;
import de.imnotpan.orderinventory.domain.inventory.InventoryItem;
import de.imnotpan.orderinventory.domain.inventory.exception.InventoryNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReserveInventoryService {

    private final InventoryRepository repository;

    public ReserveInventoryService(InventoryRepository repository){
        this.repository = repository;
    }
    
    @Transactional 
    public void reserve(long productId, long quantity){
        InventoryItem item = repository.findById(productId)
            .orElseThrow(() -> new InventoryNotFoundException(productId));
        item.reserve(quantity);
    }
}
