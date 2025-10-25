package vn.uit.clothesshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.service.ProductService.CreationError;
import vn.uit.clothesshop.service.ProductService.DeletionError;
import vn.uit.clothesshop.service.ProductService.UpdateError;

@Service
@Slf4j
public class ProductTxService {
    static final class CreationException extends RuntimeException {
        @NotNull
        private final CreationError error;

        private CreationException(@NotNull final CreationError error) {
            this.error = error;
        }

        @NotNull
        public CreationError getError() {
            return this.error;
        }
    }

    static final class UpdateException extends RuntimeException {
        @NotNull
        private final UpdateError error;

        private UpdateException(@NotNull final UpdateError error) {
            this.error = error;
        }

        @NotNull
        public UpdateError getError() {
            return this.error;
        }
    }

    static final class DeletionException extends RuntimeException {
        @NotNull
        private final DeletionError error;

        private DeletionException(@NotNull final DeletionError error) {
            this.error = error;
        }

        @NotNull
        public DeletionError getError() {
            return this.error;
        }
    }

    @NotNull
    private final ProductRepository productRepository;

    @NotNull
    private final ProductLookupService productLookupService;

    @NotNull
    private final CategoryService categoryService;

    public ProductTxService(
            @NotNull final ProductRepository productRepository,
            @NotNull final ProductLookupService productLookupService,
            @NotNull final CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productLookupService = productLookupService;
        this.categoryService = categoryService;
    }

    @Transactional(rollbackFor = CreationException.class)
    long handleCreateProduct(
            @NotNull final ProductCreationRequestDto requestDto)
            throws CreationException {
        final var categoryId = requestDto.getCategoryId();
        final var categoryUpdated = this.categoryService.increaseAmountOfProduct(categoryId, 1);
        if (!categoryUpdated) {
            throw new CreationException(CreationError.CANNOT_UPDATE_CATEGORY);
        }

        final var category = this.categoryService.findById(categoryId);
        final var product = new Product(
                requestDto.getName(),
                requestDto.getShortDesc(),
                requestDto.getDetailDesc(),
                category,
                requestDto.getTargets());

        final var savedProduct = this.handleSaveProduct(product);
        if (savedProduct == null) {
            throw new CreationException(CreationError.CANNOT_SAVE_PRODUCT);
        }

        return savedProduct.getId();
    }

    @Transactional(rollbackFor = UpdateException.class)
    public void handleUpdateProduct(
            final long id,
            @NotNull final ProductUpdateRequestDto requestDto)
            throws UpdateException {
        final var product = this.productLookupService.findById(id);
        if (product == null) {
            throw new UpdateException(UpdateError.PRODUCT_NOT_EXISTED);
        }

        final var oldCategoryId = product.getCategoryId();
        final var newCategoryId = requestDto.getCategoryId();
        if (oldCategoryId != newCategoryId) {
            final var oldCategoryUpdated = this.categoryService.decreaseAmountOfProduct(oldCategoryId, 1);
            if (!oldCategoryUpdated) {
                throw new UpdateException(UpdateError.CANNOT_UPDATE_OLD_CATEGORY);
            }

            final var newCategoryUpdated = this.categoryService.increaseAmountOfProduct(newCategoryId, 1);
            if (!newCategoryUpdated) {
                throw new UpdateException(UpdateError.CANNOT_UPDATE_NEW_CATEGORY);
            }

            final var newCategory = this.categoryService.getReferenceById(newCategoryId);
            product.setCategory(newCategory);
        }

        product.setName(requestDto.getName());
        product.setShortDesc(requestDto.getShortDesc());
        product.setDetailDesc(requestDto.getDetailDesc());
        product.setTarget(requestDto.getTargets());

        final var savedProduct = this.handleSaveProduct(product);
        if (savedProduct == null) {
            throw new UpdateException(UpdateError.CANNOT_SAVE_PRODUCT);
        }
    }

    @Transactional
    void deleteProductById(final long id)
            throws DeletionException {
        final var product = this.productLookupService.findById(id);
        if (product == null) {
            throw new DeletionException(DeletionError.PRODUCT_NOT_EXISTED);
        }

        final var categoryUpdated = this.categoryService.decreaseAmountOfProduct(product.getCategoryId(), 0);
        if (!categoryUpdated) {
            throw new DeletionException(DeletionError.CANNOT_UPDATE_CATEGORY);
        }

        try {
            // TODO: cascade to delete variant as well without using varian repo.
            this.productRepository.deleteById(id);
        } catch (final Exception exception) {
            log.error("Error deleting Product", exception);
            throw new DeletionException(DeletionError.CANNOT_DELETE_PRODUCT);
        }
    }

    @Nullable
    Product handleSaveProduct(@NotNull final Product product) {
        try {
            return this.productRepository.save(product);
        } catch (final Exception exception) {
            log.error("Error saving Product", exception);
            return null;
        }
    }
}
