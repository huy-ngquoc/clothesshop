package vn.uit.clothesshop.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.customexception.NotFoundException;
import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.dto.response.ProductBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.utils.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
@Slf4j
public class ProductService {
    @NotNull
    private final ProductRepository productRepository;

    @NotNull
    private final ProductVariantService productVariantService;

    @NotNull
    private final CategoryService categoryService;

    public ProductService(
            @NotNull final ProductRepository productRepository,
            @NotNull ProductVariantService productVariantService,
            final CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productVariantService = productVariantService;
        this.categoryService = categoryService;
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
        return this.productRepository.findAll();
    }

    @NotNull
    public Page<@NotNull ProductBasicInfoResponseDto> handleFindAllProduct(
            @NotNull final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.findAllProduct(spec, pageable)
                .map((final var product) -> new ProductBasicInfoResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getShortDesc()));
    }

    @NotNull
    public Page<@NotNull Product> findAllProduct(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.productRepository.findAll(spec, pageable);
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
        Product p = this.productRepository.findById(id).orElse(null);
        if (p == null) {
            throw new NotFoundException(Message.productNotFound);
        }
        return p;
    }

    public boolean existsProductById(final long id) {
        return this.productRepository.existsById(id);
    }

    @Nullable
    public Long handleCreateProduct(
            @NotNull final ProductCreationRequestDto requestDto) {
        Category category = categoryService.findById(requestDto.getCategoryId());
        category.setAmountOfProduct(category.getAmountOfProduct() + 1);
        final var product = new Product(
                requestDto.getName(),
                requestDto.getShortDesc(),
                requestDto.getDetailDesc(),
                category,
                requestDto.getTargets());

        final var savedProduct = this.handleSaveProduct(product);
        if (savedProduct == null) {
            return null;
        }

        return savedProduct.getId();
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
        productRepository.save(p);

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
                product.getCategoryId(), product.getTarget());
    }

    @Transactional
    public boolean handleUpdateProduct(
            final long id,
            @NotNull final ProductUpdateRequestDto requestDto) {
        final var product = this.findProductById(id);
        if (product == null) {
            return false;
        }

        final var oldCategoryId = product.getCategoryId();
        final var newCategoryId = requestDto.getCategoryId();
        if (oldCategoryId != newCategoryId) {
            if (!this.categoryService.increaseAmountOfProduct(newCategoryId, 1)) {
                return false;
            }

            if (!this.categoryService.decreaseAmountOfProduct(oldCategoryId, 1)) {
                return false;
            }

            final var newCategory = this.categoryService.getReferenceById(newCategoryId);
            product.setCategory(newCategory);
        }

        product.setName(requestDto.getName());
        product.setShortDesc(requestDto.getShortDesc());
        product.setDetailDesc(requestDto.getDetailDesc());
        product.setTarget(requestDto.getTargets());

        return this.handleSaveProduct(product) != null;
    }

    @Transactional
    public void deleteProductById(final long id) {
        final var product = findProductById(id);
        if (product == null) {
            return;
        }

        final var categoryId = product.getCategoryId();
        this.categoryService.decreaseAmountOfProduct(categoryId, 1);

        // TODO: cascade to delete variant as well without using varian repo.
        this.productRepository.deleteById(id);
    }

    @Nullable
    private Product handleSaveProduct(@NotNull final Product product) {
        try {
            return this.productRepository.save(product);
        } catch (final Exception exception) {
            log.error("Error saving Product", exception);
            return null;
        }
    }
}
