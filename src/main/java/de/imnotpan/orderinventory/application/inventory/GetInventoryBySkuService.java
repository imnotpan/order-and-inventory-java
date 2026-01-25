package de.imnotpan.orderinventory.application.inventory;

import de.imnotpan.orderinventory.application.product.GetProductService;
import de.imnotpan.orderinventory.domain.inventory.InventoryItem;
import de.imnotpan.orderinventory.domain.inventory.exception.InventoryNotFoundException;
import de.imnotpan.orderinventory.persistence.inventory.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetInventoryBySkuService {
    private final GetProductService getProductService;
    private final InventoryRepository inventoryRepository;

    public GetInventoryBySkuService(
        GetProductService getProductService,
        InventoryRepository inventoryRepository
    ){
        this.getProductService = getProductService;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public InventoryItem getBySku(String sku) {
        long productId = getProductService.getProductBySku(sku);
        return inventoryRepository.findById(productId)
            .orElseThrow(() -> new InventoryNotFoundException(productId));
    }
}
