package de.imnotpan.orderinventory.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.imnotpan.orderinventory.application.inventory.RestockInventoryService;

@Service 
public class RestockProductBySkuService {
    private final GetProductService getProductService;
    private final RestockInventoryService restockInventoryService;

    public RestockProductBySkuService(
        GetProductService getProductService,
        RestockInventoryService restockInventoryService
    ){
        this.getProductService = getProductService;
        this.restockInventoryService = restockInventoryService;
    }

    @Transactional
    public void restockBySku(String sku, long quantity){
        long productId = getProductService.getProductBySku(sku);
        restockInventoryService.restock(productId, quantity);
    }
}
