package vn.uit.clothesshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequest;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateRequest;
import vn.uit.clothesshop.repository.ProductVariantRepository;

@Service
@Slf4j
public class ProductVariantService {
    private static final String IMAGE_SUBFOLDER_NAME="productvariant";
    @NotNull
    private ProductVariantRepository productVariantRepository;

    @NotNull
    private ProductService productService;
    @NotNull
    private ImageFileService imageFileService;

    public ProductVariantService(
            @NotNull final ProductVariantRepository productVariantRepository,
            @NotNull final ProductService productService,
            @NotNull final ImageFileService imageFileService) {
        this.productVariantRepository = productVariantRepository;
        this.productService = productService;
        this.imageFileService= imageFileService;
    }

    @Nullable
    public ProductVariant findProductVariantById(final long id) {
        return this.productVariantRepository.findById(id).orElse(null);
    }

    @NotNull
    public List<ProductVariant> findAllProductVariantsByProductId(final long productId) {
        final var product = this.productService.findProductById(productId);
        if (product == null) {
            return new ArrayList<>(0);
        }

        return this.findAllProductVariantsByProduct(product);
    }

    @Nullable
    public List<ProductVariant> findAllProductVariantsByProduct(@NotNull final Product product) {
        return this.productVariantRepository.findAllByProduct(product);
    }

    @Nullable
    private ProductVariant handleSaveProductVariant(final ProductVariant productVariant) {
        try {
            return this.productVariantRepository.save(productVariant);
        } catch (final Exception exception) {
            log.error("Error saving product variant", exception);
            return null;
        }
    }
    public ProductVariant createProductVariant(ProductVariantCreateRequest request) {
        Product product = productService.findProductById(request.getProductId());
        if(product==null) {
             System.out.println("NULLLLLL");
            return null;
        }
        ProductVariant newVariant = new ProductVariant(product, request.getColor(), request.getSize(), request.getStockQuantity(), request.getPriceCents(), request.getWeightGrams(),"");
        newVariant=productVariantRepository.save(newVariant);
        return newVariant;
    }
    public ProductVariant updateInfo(ProductVariantUpdateRequest request) {
        ProductVariant pv = findProductVariantById(request.getProductVariantId());
        if(pv==null) {
            return null;
        }
        pv.setColor(request.getColor());
        pv.setSize(request.getSize());
        pv.setPriceCents(request.getPriceCents());
        pv.setStockQuantity(request.getStockQuantity());
        pv.setWeightGrams(request.getWeightGrams());
        pv=productVariantRepository.save(pv);
        return pv;
    }

    public ProductVariant updateProductVariantImage(MultipartFile image, long productVariantId) {
        ProductVariant pv = findProductVariantById(productVariantId);
        if(pv==null) {
            return null;
        }
        String imageFileName = pv.getImage();
        if(!StringUtils.hasText(imageFileName)) {
            imageFileName=imageFileService.handleSaveUploadFile(image, IMAGE_SUBFOLDER_NAME);
        } 
        else {
            imageFileName=imageFileService.handleUpdateUploadFile(imageFileName, image, IMAGE_SUBFOLDER_NAME);
        }
        pv.setImage(imageFileName);
        pv=productVariantRepository.save(pv);
        return pv;
    }

    public long deleteProductVariant(long id) {
        ProductVariant pv = findProductVariantById(id);
        if(pv==null) {
            return -1;
        } 
        
        productVariantRepository.delete(pv);
        return pv.getProduct().getId();
    }


    
}
