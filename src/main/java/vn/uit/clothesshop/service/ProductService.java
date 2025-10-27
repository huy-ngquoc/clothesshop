package vn.uit.clothesshop.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.dto.response.ProductHomepageInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.service.ProductTxService.CreationException;
import vn.uit.clothesshop.service.ProductTxService.DeletionException;
import vn.uit.clothesshop.service.ProductTxService.UpdateException;
import vn.uit.clothesshop.utils.Expected;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
@Slf4j
public class ProductService {
    public enum CreationError {
        CANNOT_UPDATE_CATEGORY,
        CANNOT_SAVE_PRODUCT,
    }

    public enum UpdateError {
        PRODUCT_NOT_EXISTED,
        CANNOT_UPDATE_OLD_CATEGORY,
        CANNOT_UPDATE_NEW_CATEGORY,
        CANNOT_SAVE_PRODUCT,
    }

    public enum DeletionError {
        PRODUCT_NOT_EXISTED,
        CANNOT_UPDATE_CATEGORY,
        CANNOT_DELETE_PRODUCT,
    }

    private static final String IMAGE_SUB_FOLDER_NAME = "productvariant";

    @NotNull
    private final ProductLookupService productLookupService;

    @NotNull
    private final ProductTxService productTxService;

    @NotNull
    private final ProductVariantService productVariantService;

    @NotNull
    private final ImageFileService imageFileService;

    public ProductService(
            @NotNull final ProductLookupService productLookupService,
            @NotNull final ProductTxService productTxService,
            @NotNull final ProductVariantService productVariantService,
            @NotNull final ImageFileService imageFileService) {
        this.productLookupService = productLookupService;
        this.productTxService = productTxService;
        this.productVariantService = productVariantService;
        this.imageFileService = imageFileService;
    }

    @NotNull
    public List<@NotNull ProductBasicInfoResponseDto> handleFindAllProduct() {
        return this.findAllProduct()
                .stream()
                .map((final var product) -> new ProductBasicInfoResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getShortDesc()))
                .toList();
    }

    @NotNull
    public List<@NotNull Product> findAllProduct() {
        return this.productLookupService.findAll();
    }

    @NotNull
    public Page<@NotNull ProductBasicInfoResponseDto> handleFindAllProduct(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.findAllProduct(spec, pageable)
                .map((final var product) -> new ProductBasicInfoResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getShortDesc()));
    }

    @NotNull
    public Page<@NotNull ProductHomepageInfoResponseDto> handleFindAllProductForHomepage(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.findAllProduct(spec, pageable)
                .map((final var product) -> new ProductHomepageInfoResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getShortDesc(),
                        this.imageFileService.getPathString(product.getImage(), IMAGE_SUB_FOLDER_NAME),
                        product.getMinPrice(),
                        product.getMaxPrice()));
    }

    @NotNull
    public Page<@NotNull Product> findAllProduct(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.productLookupService.findAll(spec, pageable);
    }

    @Nullable
    public ProductDetailInfoResponseDto handleFindProductById(final long id) {
        final var product = this.findProductById(id);
        if (product == null) {
            return null;
        }

        return new ProductDetailInfoResponseDto(
                product.getName(),
                product.getShortDesc(),
                product.getDetailDesc(),
                productVariantService.handleFindAllProductVariantsByProduct(product), product.getMinPrice(),
                product.getMaxPrice(),
                product.getSold(), product.getQuantity(), product.getImage());
    }

    @Nullable
    public Product findProductById(final long id) {
        return this.productLookupService.findById(id);
    }

    public boolean existsProductById(final long id) {
        return this.productLookupService.existsById(id);
    }

    @Nullable
    public Expected<Long, CreationError> handleCreateProduct(
            @NotNull final ProductCreationRequestDto requestDto) {
        try {
            return Expected.success(this.productTxService.handleCreateProduct(requestDto));
        } catch (final CreationException exception) {
            return Expected.failure(exception.getError());
        }
    }

    public void updateMinPriceAndMaxPrice(Product p) {
        if (p == null) {
            return;
        }
        List<ProductVariant> listVariants = this.productVariantService.findAllProductVariantsByProduct(p);
        ProductVariant mostExpensiveVariant = Collections.max(listVariants);
        p.setMaxPrice(mostExpensiveVariant.getPriceCents());
        ProductVariant cheapestVariant = Collections.min(listVariants);
        p.setMinPrice(cheapestVariant.getPriceCents());
        this.productTxService.handleSaveProduct(p);

    }

    @Nullable
    public ProductUpdateRequestDto handleCreateRequestDtoForUpdate(final long id) {
        final var product = this.findProductById(id);
        if (product == null) {
            return null;
        }

        return new ProductUpdateRequestDto(
                product.getName(),
                product.getShortDesc(),
                product.getDetailDesc(),
                product.getCategoryId(),
                product.getTarget());
    }

    @Nullable
    public UpdateError handleUpdateProduct(
            final long id,
            @NotNull final ProductUpdateRequestDto requestDto) {
        try {
            this.productTxService.handleUpdateProduct(id, requestDto);
            return null;
        } catch (final UpdateException exception) {
            return exception.getError();
        }
    }

    @Nullable
    public DeletionError deleteProductById(final long id) {
        try {
            this.productTxService.deleteProductById(id);
            return null;
        } catch (final DeletionException exception) {
            return exception.getError();
        }
    }
}
