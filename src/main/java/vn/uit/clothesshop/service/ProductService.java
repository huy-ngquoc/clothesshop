package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.dto.response.FullProductDataDto;
import vn.uit.clothesshop.dto.response.ProductBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.dto.selectcolumninteface.ProductVariantInfo;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.repository.ProductVariantRepository;

@Service
@Slf4j
public class ProductService {
    @NotNull
    private final ProductRepository productRepository;
    @NotNull
    private final ProductVariantRepository productVariantRepository;

    public ProductService(
            @NotNull final ProductRepository productRepository, ProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.productVariantRepository= productVariantRepository;
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

    @Nullable
    public ProductDetailInfoResponseDto handleFindProductById(final long id) {
        final var product = this.findProductById(id);
        if (product == null) {
            return null;
        }

        return new ProductDetailInfoResponseDto(
                product.getName(),
                product.getShortDesc(),
                product.getDetailDesc());
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Nullable
    public Product findProductById(final long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Nullable
    public Long handleCreateProduct(
            @NotNull final ProductCreationRequestDto requestDto) {
        final var product = new Product(
                requestDto.getName(),
                requestDto.getShortDesc(),
                requestDto.getDetailDesc());

        final var savedProduct = this.handleSaveProduct(product);
        if (savedProduct == null) {
            return null;
        }

        return savedProduct.getId();
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
                product.getDetailDesc());
    }

    public boolean handleUpdateProduct(
            final long id,
            @NotNull final ProductUpdateRequestDto requestDto) {
        final var product = this.findProductById(id);
        if (product == null) {
            return false;
        }

        product.setName(requestDto.getName());
        product.setShortDesc(requestDto.getShortDesc());
        product.setDetailDesc(requestDto.getDetailDesc());

        return this.handleSaveProduct(product) != null;
    }

    public void deleteProductById(final long id) {
        this.productRepository.deleteById(id);
    }

    @Nullable
    private Product handleSaveProduct(final Product product) {
        try {
            return this.productRepository.save(product);
        } catch (final Exception exception) {
            log.error("Error saving product", exception);
            return null;
        }
    }

    public FullProductDataDto getFullDataById(long id) {
        Product p = productRepository.findById(id).orElse(null);
        if(p==null) {
            return null;
        } 
        List<ProductVariantInfo> listVariants = productVariantRepository.findByProduct_Id(p.getId());
        return new FullProductDataDto(p.getId(), p.getName(), p.getShortDesc(), p.getDetailDesc(), listVariants);
    }
}
