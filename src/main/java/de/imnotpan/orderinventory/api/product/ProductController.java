package de.imnotpan.orderinventory.api.product;
import de.imnotpan.orderinventory.api.product.dto.CreateProductRequest;
import de.imnotpan.orderinventory.api.product.dto.PagedResponse;
import de.imnotpan.orderinventory.api.product.dto.ProductResponse;

import de.imnotpan.orderinventory.application.product.CreateProductService;
import de.imnotpan.orderinventory.application.product.GetProductService;
import de.imnotpan.orderinventory.application.product.ListProductsService;
import de.imnotpan.orderinventory.domain.product.Product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final CreateProductService createProductService;
    private final ListProductsService listProductsService;
    private final GetProductService getProductService;

    public ProductController(CreateProductService createProductService,
                             ListProductsService listProductsService,
                                GetProductService getProductService) {
        this.createProductService = createProductService;
        this.listProductsService = listProductsService;
        this.getProductService = getProductService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public ProductResponse create(@Valid @RequestBody CreateProductRequest request){
        Product product = createProductService.create(
                request.sku(),
                request.name(),
                request.price()
        );

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.isActive()
        );
    }

    @GetMapping
    public PagedResponse<ProductResponse> list(
            @PageableDefault(size = 10) Pageable pageable
    ){
        Page<Product> page = listProductsService.list(pageable);
        return new PagedResponse<>(
                page.getContent().stream()
                        .map(product -> new ProductResponse(
                                product.getId(),
                                product.getSku(),
                                product.getName(),
                                product.getPrice(),
                                product.isActive()
                        ))
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        Product product = getProductService.getById(id);

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.isActive()
        );
    }
    
}
