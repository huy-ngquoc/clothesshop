package vn.uit.clothesshop.service;


import static java.lang.Math.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.customexception.NotFoundException;
import vn.uit.clothesshop.customexception.UnexpectedException;
import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.domain.entity.Product_;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.dto.response.ProductBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.specification.ProductSpecification;
import vn.uit.clothesshop.utils.Message;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import vn.uit.clothesshop.utils.ParamValidator;



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
                productVariantService.handleFindAllProductVariantsByProduct(product), product.getMinPrice(), product.getMaxPrice(), 
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
                requestDto.getDetailDesc(),"",   category, requestDto.getTargets(), Instant.now(),null,0,0);

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
                product.getDetailDesc(), product.getCategory().getId(), product.getTarget());
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
        if (oldCat.getId() != newCat.getId()) {
            oldCat.setAmountOfProduct(oldCat.getAmountOfProduct() - 1);
            newCat.setAmountOfProduct(newCat.getAmountOfProduct() + 1);
            categoryService.handleSaveCategory(oldCat);
            product.setCategory(newCat);
        }
        product.setTarget(requestDto.getTargets());
        return this.handleSaveProduct(product) != null;
    }

    public void deleteProductById(final long id) {
        Product p = findProductById(id);
        Category cat = p.getCategory();
        if (cat == null) {
            throw new UnexpectedException(Message.unexpectedError);
        }
        cat.setAmountOfProduct(cat.getAmountOfProduct() - 1);
        // TODO: cascade to delete variant as well without using varian repo.
        this.productRepository.deleteById(id);
    }

    @Nullable
    private Product handleSaveProduct(@NotNull final Product product) {
        try {
            return this.productRepository.save(product);
        } catch (final Exception exception) {
           
            return null;
        }
    }

    public Page<Product> getProductByPage(int page, int number) {
        PageRequest pageable = PageRequest.of(number-1, page);
        return this.productRepository.findAll(pageable);
        
    }
    public Page<Product> getProductByPageAndName(int page, int number, String name) {
        PageRequest pageable = PageRequest.of(number-1, page);
        return this.productRepository.findAll(ProductSpecification.nameLike(name),pageable);
    }
    public Set<Long> getProductIdSetFromProductIdList(List<Long> listProductId) {
        if(!ParamValidator.validateList(listProductId)) {
            return null;
        }
        return new HashSet<>(listProductId);
    }
    public Page<Product> getProductByFilter(int page, int number, String name, Integer fromPrice, Integer toPrice, List<String> listSize, List<String> listColor) {
        Specification<Product> finalSpec = null;
        PageRequest pageable = PageRequest.of(number-1, page);
        if(name!=null&&!name.equals("")) {
            finalSpec= ProductSpecification.nameLike(name);
        } 
        if(ParamValidator.validateFromPriceAndToPrice(fromPrice, toPrice)) {
            Specification<Product> priceRangeSpec = ProductSpecification.priceBetween(fromPrice, toPrice);
            finalSpec = ProductSpecification.andTwoSpec(finalSpec, priceRangeSpec);
        }
        List<Long> listProductIds = new ArrayList<>();
        if(ParamValidator.validateList(listColor)) {
            listProductIds=new ArrayList<>(productVariantService.getProductIdByColor(listColor));
            
        } 
        if(ParamValidator.validateList(listSize)) {
            List<Long> listIdBySize = productVariantService.getProductIdBySize(listSize);
            if(listIdBySize!=null) {
                listProductIds.retainAll(listIdBySize);
            }
        }
        Set<Long> productIdSet = getProductIdSetFromProductIdList(listProductIds);
        if(ParamValidator.validateSet(productIdSet)) {
            System.out.println("Valid set");
            Specification idInSpec = ProductSpecification.idIn(productIdSet);
            finalSpec=ProductSpecification.andTwoSpec(finalSpec, idInSpec);
        }
        if(finalSpec==null) {
            return this.productRepository.findAll(pageable);
        }
        return this.productRepository.findAll(finalSpec,pageable);

    }

    
   
    

}