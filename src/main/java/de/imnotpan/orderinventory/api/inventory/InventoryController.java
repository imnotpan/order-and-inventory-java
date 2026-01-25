package de.imnotpan.orderinventory.api.inventory;

import de.imnotpan.orderinventory.api.inventory.dto.InventoryResponse;
import de.imnotpan.orderinventory.application.inventory.GetInventoryBySkuService;
import de.imnotpan.orderinventory.application.product.ReserveProductBySkuService;
import de.imnotpan.orderinventory.application.product.RestockProductBySkuService;
import de.imnotpan.orderinventory.domain.inventory.InventoryItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final RestockProductBySkuService restockBySkuService;
    private final ReserveProductBySkuService reserveBySkuService;
    private final GetInventoryBySkuService getInventoryBySkuService;

    public InventoryController(
        RestockProductBySkuService restockBySkuService,
        ReserveProductBySkuService reserveBySkuService,
        GetInventoryBySkuService getInventoryBySkuService
    ){
        this.restockBySkuService = restockBySkuService;
        this.reserveBySkuService = reserveBySkuService;
        this.getInventoryBySkuService = getInventoryBySkuService;
    }

    @GetMapping("/{sku}")
    public InventoryResponse InventoryResponse(@PathVariable String sku) {
        InventoryItem item = getInventoryBySkuService.getBySku(sku);

        return new InventoryResponse(
            sku, 
            item.getAvailableQuantity(), 
            item.getReservedQuantity()
        );
    }

    @PostMapping("/{sku}/restock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restock(@PathVariable String sku, @RequestParam long quantity) {
        restockBySkuService.restockBySku(sku, quantity);
    }
    
    @PostMapping("/{sku}/reserve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserve(@PathVariable String sku, @RequestParam int quantity) {
        reserveBySkuService.reserveBySku(sku, quantity);
    }
}
