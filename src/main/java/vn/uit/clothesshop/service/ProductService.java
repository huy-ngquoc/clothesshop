package vn.uit.clothesshop.service;

import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.repository.ProductRepository;

@Service
public class ProductService {
    @NotNull
    private final ProductRepository productRepository;

    public ProductService(
            @NotNull final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
