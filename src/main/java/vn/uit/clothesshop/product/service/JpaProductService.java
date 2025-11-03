package vn.uit.clothesshop.product.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.domain.CategoryAccess;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariantAccess;
import vn.uit.clothesshop.product.mapper.ProductMapper;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductUpdateInfoViewModel;
import vn.uit.clothesshop.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

@Service
@Transactional(readOnly = true)
@Slf4j
class JpaProductService implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryAccess categoryAccess;
    private final ProductVariantAccess productVariantAccess;

    public JpaProductService(
            final ProductRepository repository,
            final ProductMapper mapper,
            final CategoryAccess categoryAccess,
            final ProductVariantAccess productVariantAccess) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoryAccess = categoryAccess;
        this.productVariantAccess = productVariantAccess;
    }

    @Transactional
    public void updatePriceBoundById(final long id) {
        final var priceBound = this.productVariantAccess.findPriceBoundByProductId(id);
        if (priceBound == null) {
            return;
        }

        final var product = this.repository.getReferenceById(id);
        product.setMinPrice(priceBound.getMin());
        product.setMaxPrice(priceBound.getMax());
    }

    @Override
    public Page<ProductBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toCard);
    }

    @Override
    public Optional<ProductDetailInfoViewModel> findDetailById(final long id) {
        return this.repository.findById(id).map(this.mapper::toDetailInfo);
    }

    @Override
    @Transactional
    public long create(@NotNull final ProductCreationForm form) {
        final var categoryUpdated = this.categoryAccess.increaseProductCount(form.getCategoryId(), 1);
        if (!categoryUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        final var category = this.categoryAccess.getReferenceById(form.getCategoryId());
        final var product = this.mapper.toEntityOnCreate(form, category);
        return this.repository.save(product).getId();
    }

    @Override
    public Optional<ProductUpdateInfoViewModel> getUpdateInfoById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Override
    @Transactional
    public void updateInfoById(final long id, @NotNull final ProductUpdateInfoForm form) {
        final var product = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        final var oldCategoryId = product.getCategoryId();
        final var newCategoryId = form.getCategoryId();
        if (oldCategoryId == newCategoryId) {
            final var category = this.categoryAccess.getReferenceById(oldCategoryId);
            this.mapper.applyUpdateInfo(product, form, category);
            return;
        }

        final var oldCategoryUpdated = this.categoryAccess.decreaseProductCount(oldCategoryId, 1);
        if (!oldCategoryUpdated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product count of old Category not match.");
        }

        final var newCategoryUpdated = this.categoryAccess.increaseProductCount(newCategoryId, 1);
        if (!newCategoryUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "New Category not found");
        }

        final var newCategory = this.categoryAccess.getReferenceById(newCategoryId);
        this.mapper.applyUpdateInfo(product, form, newCategory);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var product = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        final var categoryUpdated = this.categoryAccess.decreaseProductCount(product.getCategoryId(), 1);
        if (!categoryUpdated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product count of Category not match.");
        }

        this.repository.delete(product);
    }
}
