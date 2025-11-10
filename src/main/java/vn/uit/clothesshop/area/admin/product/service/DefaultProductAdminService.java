package vn.uit.clothesshop.area.admin.product.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.area.admin.product.mapper.ProductAdminMapper;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.category.domain.port.CategoryReadPort;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;
import vn.uit.clothesshop.feature.product.infra.jpa.spec.ProductVariantSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Service
@Transactional(readOnly = true)
class DefaultProductAdminService implements ProductAdminService {
    private final ProductAdminMapper mapper;
    private final ProductReadPort productReadPort;
    private final ProductWritePort productWritePort;
    private final CategoryReadPort categoryPort;
    private final ProductVariantReadPort productVariantPort;

    public DefaultProductAdminService(
            final ProductAdminMapper mapper,
            final ProductReadPort productReadPort,
            final ProductWritePort productWritePort,
            final CategoryReadPort categoryPort,
            final ProductVariantReadPort productVariantPort) {
        this.mapper = mapper;
        this.productReadPort = productReadPort;
        this.productWritePort = productWritePort;
        this.categoryPort = categoryPort;
        this.productVariantPort = productVariantPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable) {
        return this.productReadPort.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable) {
        return this.productReadPort.findAll(spec, pageable).map(this.mapper::toCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductAdminDetailInfoViewModel> findDetailById(
            final long id,
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable) {
        final var product = this.productReadPort.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var finalSpec = ProductVariantSpecification
                .hasProductId(id).and(spec);
        final var productVariantPage = this.productVariantPort.findAll(finalSpec, pageable);

        return Optional.of(this.mapper.toDetailInfo(product, productVariantPage));
    }

    @Override
    @Transactional(readOnly = true)
    public Pair<ProductAdminCreationViewModel, ProductAdminCreationForm> findCreation(
            @NonNull final Pageable categoryPageable) {
        final var categoryPage = this.categoryPort.findAll(categoryPageable);
        return this.mapper.toCreation(categoryPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAdminCreationViewModel findCreationViewModel(
            @NonNull final Pageable categoryPageable) {
        final var categoryPage = this.categoryPort.findAll(categoryPageable);
        return this.mapper.toCreationViewModel(categoryPage);
    }

    @Override
    @Transactional
    public long create(@NonNull final ProductAdminCreationForm form) {
        final var categoryUpdated = this.categoryPort.increaseProductCount(form.getCategoryId(), 1);
        if (!categoryUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        final var category = this.categoryPort.getReferenceById(form.getCategoryId());
        final var product = this.mapper.toEntityOnCreate(form, category);
        return this.productWritePort.save(product).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pair<ProductAdminInfoUpdateViewModel, ProductAdminInfoUpdateForm>> findInfoUpdateById(
            final long id,
            @NonNull final Pageable categoryPageable) {
        final var product = this.productReadPort.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var categoryPage = this.categoryPort.findAll(categoryPageable);
        return Optional.of(this.mapper.toInfoUpdate(product, categoryPage));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductAdminInfoUpdateViewModel> findInfoUpdateViewModelById(
            final long id,
            @NonNull final Pageable categoryPageable) {
        final var product = this.productReadPort.findById(id).orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        final var categoryPage = this.categoryPort.findAll(categoryPageable);
        return Optional.of(this.mapper.toInfoUpdateViewModel(product, categoryPage));
    }

    @Override
    @Transactional
    public void updateInfoById(final long id, @NonNull final ProductAdminInfoUpdateForm form) {
        final var product = this.productReadPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        final var oldCategoryId = product.getCategoryId();
        final var newCategoryId = form.getCategoryId();
        if (oldCategoryId == newCategoryId) {
            final var category = this.categoryPort.getReferenceById(oldCategoryId);
            this.mapper.applyUpdateInfo(product, form, category);
            return;
        }

        final var oldCategoryUpdated = this.categoryPort.decreaseProductCount(oldCategoryId, 1);
        if (!oldCategoryUpdated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product count of old Category not match.");
        }

        final var newCategoryUpdated = this.categoryPort.increaseProductCount(newCategoryId, 1);
        if (!newCategoryUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "New Category not found");
        }

        final var newCategory = this.categoryPort.getReferenceById(newCategoryId);
        this.mapper.applyUpdateInfo(product, form, newCategory);

        this.productWritePort.save(product);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var product = this.productReadPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        final var categoryUpdated = this.categoryPort.decreaseProductCount(product.getCategoryId(), 1);
        if (!categoryUpdated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product count of Category not match.");
        }

        this.productWritePort.delete(product);
    }
}
