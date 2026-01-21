package de.imnotpan.orderinventory.application.product;

import org.springframework.stereotype.Service;

import de.imnotpan.orderinventory.domain.exception.ProductNotFoundException;
import de.imnotpan.orderinventory.domain.product.Product;

import de.imnotpan.orderinventory.persistence.product.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetProductService {
    
    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Product getById(Long id){
        return productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
