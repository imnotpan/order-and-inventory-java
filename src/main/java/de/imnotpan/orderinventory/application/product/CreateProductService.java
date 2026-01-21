package de.imnotpan.orderinventory.application.product;

import de.imnotpan.orderinventory.domain.exception.ProductAlreadyExistsException;
import de.imnotpan.orderinventory.domain.inventory.InventoryItem;
import de.imnotpan.orderinventory.domain.product.Product;
import de.imnotpan.orderinventory.persistence.inventory.InventoryRepository;
import de.imnotpan.orderinventory.persistence.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public CreateProductService(ProductRepository productRepository,
                                InventoryRepository inventoryRepository){
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Product create(String sku, String name, BigDecimal price){
        if(productRepository.existsBySku(sku)){
            throw new ProductAlreadyExistsException(sku);
        }
        Product product = new Product(sku, name, price);
        Product savedProduct = productRepository.save(product);

        InventoryItem inventory = new InventoryItem(savedProduct.getId(), 0);
        inventoryRepository.save(inventory);

        return savedProduct;

    }
}
