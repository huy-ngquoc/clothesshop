package vn.uit.clothesshop.product.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductAccess;
import vn.uit.clothesshop.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
class JpaProductAccess implements ProductAccess {
    private final ProductRepository repository;

    public JpaProductAccess(final ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(final long id) {
        return this.repository.findById(id);
    }

    @Override
    public boolean existsById(final long id) {
        return this.repository.existsById(id);
    }
}
