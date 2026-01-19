package de.imnotpan.orderinventory.application.product;

import de.imnotpan.orderinventory.domain.product.Product;
import de.imnotpan.orderinventory.persistence.product.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListProductsService {
    private final ProductRepository productRepository;
    public ListProductsService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> list(Pageable pageable){
        return productRepository.findAll(pageable);
    }

}
