package vn.uit.clothesshop.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.customexception.NotFoundException;
import vn.uit.clothesshop.customexception.UnexpectedException;
import vn.uit.clothesshop.domain.Category;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.dto.response.FullProductDataDto;
import vn.uit.clothesshop.dto.response.ProductBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.dto.selectcolumninteface.ProductVariantInfo;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.repository.ProductVariantRepository;
import vn.uit.clothesshop.utils.Message;

@Service
@Slf4j
public class ProductService {
    @NotNull
    private final ProductRepository productRepository;
    @NotNull
    private final ProductVariantRepository productVariantRepository;
    @NotNull
    private final CategoryService categoryService;

    public ProductService(
            @NotNull final ProductRepository productRepository, ProductVariantRepository productVariantRepository, final CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productVariantRepository= productVariantRepository;
        this.categoryService= categoryService;
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
        Product p=this.productRepository.findById(id).orElse(null);
        if(p==null) {
            throw new NotFoundException(Message.productNotFound);
        }
        return p;
    }

    @Nullable
    public Long handleCreateProduct(
            @NotNull final ProductCreationRequestDto requestDto) {
        Category category = categoryService.findById(requestDto.getCategoryId());
        category.setAmountOfProduct(category.getAmountOfProduct()+1);
        final var product = new Product(
                requestDto.getName(),
                requestDto.getShortDesc(),
                requestDto.getDetailDesc(),0,0,category,requestDto.getTargets());

        final var savedProduct = this.handleSaveProduct(product);
        if (savedProduct == null) {
            return null;
        }

        return savedProduct.getId();
    }

    public void updateMinPriceAndMaxPrice(Product p) {
        if(p==null) {
            return;
        }
        List<ProductVariant> listVariants = productVariantRepository.findAllByProduct(p);
        ProductVariant mostExpensiveVariant= Collections.max(listVariants);
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
                product.getDetailDesc(), product.getCategory().getId(),product.getTarget());
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
        Category oldCat = product.getCategory();
        Category newCat = categoryService.findById(requestDto.getCategoryId());
        if(oldCat.getId()!=newCat.getId()) {
            oldCat.setAmountOfProduct(oldCat.getAmountOfProduct()-1);
            newCat.setAmountOfProduct(newCat.getAmountOfProduct()+1);
            categoryService.handleSaveCategory(oldCat);
            product.setCategory(newCat);
        }
        product.setTarget(requestDto.getTargets());
        return this.handleSaveProduct(product) != null;
    }

    public void deleteProductById(final long id) {
        Product p = findProductById(id);
        Category cat = p.getCategory();
        if(cat==null) {
            throw new UnexpectedException(Message.unexpectedError);
        }
        cat.setAmountOfProduct(cat.getAmountOfProduct()-1);
        this.productVariantRepository.deleteByProduct_Id(id);
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
