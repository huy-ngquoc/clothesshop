package vn.uit.clothesshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequestDto;
import vn.uit.clothesshop.dto.response.ProductVariantBasicInfoResponseDto;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.repository.ProductVariantRepository;

@Service
@Slf4j
public class ProductVariantService {
    @NotNull
    private final ProductVariantRepository productVariantRepository;

    @NotNull
    private final ProductRepository productRepository;

    public ProductVariantService(
            @NotNull final ProductVariantRepository productVariantRepository,
            @NotNull final ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    @Nullable
    public ProductVariant findProductVariantById(final long id) {
        return this.productVariantRepository.findById(id).orElse(null);
    }

    @NotNull
    public List<ProductVariant> findAllProductVariantsByProductId(final long productId) {
        final var product = this.productRepository.findById(productId).orElse(null);
        if (product == null) {
            return new ArrayList<>(0);
        }

        return this.findAllProductVariantsByProduct(product);
    }

    @NotNull
    public List<@NotNull ProductVariantBasicInfoResponseDto> handleFindAllProductVariantsByProduct(
            @NotNull final Product product) {
        return this.findAllProductVariantsByProduct(product)
                .stream()
                .map((final var productVariant) -> new ProductVariantBasicInfoResponseDto(
                        productVariant.getId(),
                        productVariant.getColor(),
                        productVariant.getSize(),
                        productVariant.getStockQuantity(),
                        productVariant.getWeightGrams()))
                .toList();
    }

    @Nullable
    public List<ProductVariant> findAllProductVariantsByProduct(@NotNull final Product product) {
        return this.productVariantRepository.findAllByProduct(product);
    }

    @Nullable
    public Long createProductVariant(
            final long productId,
            @NotNull final ProductVariantCreateRequestDto requestDto) {
        final var product = this.productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        final var productVariant = new ProductVariant(
                product,
                requestDto.getColor(),
                requestDto.getSize(),
                requestDto.getStockQuantity(),
                requestDto.getPriceCents(),
                requestDto.getWeightGrams());

        final var savedProductVariant = productVariantRepository.save(productVariant);
        return savedProductVariant.getId();
    }

    @Nullable
    private ProductVariant handleSaveProductVariant(@NotNull final ProductVariant productVariant) {
        try {
            return this.productVariantRepository.save(productVariant);
        } catch (final Exception exception) {
            log.error("Error saving product variant", exception);
            return null;
        }
    }

}
