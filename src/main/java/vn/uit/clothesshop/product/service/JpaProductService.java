package vn.uit.clothesshop.product.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.domain.CategoryAccess;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.domain.ProductVariantAccess;
import vn.uit.clothesshop.product.domain.specification.ProductVariantSpecification;
import vn.uit.clothesshop.product.mapper.ProductMapper;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductInfoUpdateForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCreationViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductInfoUpdateViewModel;
import vn.uit.clothesshop.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

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
            @NonNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toCard);
    }

    @Override
    public Optional<ProductDetailInfoViewModel> findDetailById(
            final long id,
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable) {
        final var product = this.repository.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var finalSpec = ProductVariantSpecification
                .hasProductId(id).and(spec);
        final var productVariantPage = this.productVariantAccess.findAll(finalSpec, pageable);

        return Optional.of(this.mapper.toDetailInfo(product, productVariantPage));
    }

    @Override
    public Pair<ProductCreationViewModel, ProductCreationForm> findCreation(
            @NonNull final Pageable categoryPageable) {
        final var categoryPage = this.categoryAccess.findAll(categoryPageable);
        return this.mapper.toCreation(categoryPage);
    }

    @Override
    public ProductCreationViewModel findCreationViewModel(
            @NonNull final Pageable categoryPageable) {
        final var categoryPage = this.categoryAccess.findAll(categoryPageable);
        return this.mapper.toCreationViewModel(categoryPage);
    }

    @Override
    @Transactional
    public long create(@NonNull final ProductCreationForm form) {
        final var categoryUpdated = this.categoryAccess.increaseProductCount(form.getCategoryId(), 1);
        if (!categoryUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        final var category = this.categoryAccess.getReferenceById(form.getCategoryId());
        final var product = this.mapper.toEntityOnCreate(form, category);
        return this.repository.save(product).getId();
    }

    @Override
    public Optional<Pair<ProductInfoUpdateViewModel, ProductInfoUpdateForm>> findInfoUpdateById(
            final long id,
            @NonNull final Pageable categoryPageable) {
        final var product = this.repository.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var categoryPage = this.categoryAccess.findAll(categoryPageable);
        return Optional.of(this.mapper.toInfoUpdate(product, categoryPage));
    }

    @Override
    public Optional<ProductInfoUpdateViewModel> findInfoUpdateViewModelById(
            final long id,
            @NonNull final Pageable categoryPageable) {
        final var product = this.repository.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var categoryPage = this.categoryAccess.findAll(categoryPageable);
        return Optional.of(this.mapper.toInfoUpdateViewModel(product, categoryPage));
    }

    @Override
    @Transactional
    public void updateInfoById(final long id, @NonNull final ProductInfoUpdateForm form) {
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
