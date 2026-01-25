package de.imnotpan.orderinventory.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.imnotpan.orderinventory.application.inventory.ReserveInventoryService;


@Service
public class ReserveProductBySkuService {
    private final GetProductService getProductService;
    private final ReserveInventoryService reserveInventoryService;

    public ReserveProductBySkuService(
        GetProductService getProductService,
        ReserveInventoryService reserveInventoryService
    ){
        this.getProductService = getProductService;
        this.reserveInventoryService = reserveInventoryService;
    }

    @Transactional
    public void reserveBySku(String sku, long quantity){
        long productId = getProductService.getProductBySku(sku);
        reserveInventoryService.reserve(productId, quantity);
    }
}
