package vn.uit.clothesshop.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.dto.middle.ProductVariantUpdateImageMiddleDto;
import vn.uit.clothesshop.dto.middle.ProductVariantUpdateInfoMiddleDto;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequestDto;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateImageRequestDto;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateRequestDto;
import vn.uit.clothesshop.dto.response.ProductVariantBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductVariantDetailInfoResponseDto;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.dto.selectcolumninteface.ColorCount;
import vn.uit.clothesshop.dto.selectcolumninteface.GetProductId;
import vn.uit.clothesshop.dto.selectcolumninteface.ProductInfoHomePage;
import vn.uit.clothesshop.dto.selectcolumninteface.SizeCount;
import vn.uit.clothesshop.repository.ProductVariantRepository;

@Service
@Slf4j
public class ProductVariantService {
    private static final String IMAGE_SUBFOLDER_NAME = "productvariant";
    @NotNull
    private final ProductVariantRepository productVariantRepository;

    @NotNull
    private final ProductRepository productRepository;

    @NotNull
    private final ImageFileService imageFileService;

    public ProductVariantService(
            @NotNull final ProductVariantRepository productVariantRepository,
            @NotNull final ProductRepository productRepository,
            @NotNull final ImageFileService imageFileService) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
        this.imageFileService = imageFileService;
    }

    public boolean existsProductVariantById(final long id) {
        return this.productVariantRepository.existsById(id);
    }

    @Nullable
    public ProductVariantDetailInfoResponseDto handleFindProductVarianById(final long id) {
        final var productVariant = this.findProductVariantById(id);
        if (productVariant == null) {
            return null;
        }

        final var imageFilePath = this.imageFileService.getPathString(productVariant.getImage(), IMAGE_SUBFOLDER_NAME);

        return new ProductVariantDetailInfoResponseDto(
                productVariant.getProductId(),
                productVariant.getColor(),
                productVariant.getSize(),
                productVariant.getStockQuantity(),
                productVariant.getPriceCents(),
                productVariant.getWeightGrams(),
                imageFilePath);
    }

    @Nullable
    public ProductVariant findProductVariantById(final long id) {
        return this.productVariantRepository.findById(id).orElse(null);
    }

    @Nullable
    public String findImageFilePathOfProductVariantById(final long id) {
        final var productVariant = this.findProductVariantById(id);
        if (productVariant == null) {
            return null;
        }

        return this.imageFileService.getPathString(
                productVariant.getImage(),
                IMAGE_SUBFOLDER_NAME);
    }

    @NotNull
    public List<ProductVariant> findAllProductVariantsByProductId(final long productId) {
        return this.productVariantRepository.findByProduct_Id(productId);
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
                        productVariant.getPriceCents(),
                        productVariant.getWeightGrams(),
                        this.imageFileService.getPathString(productVariant.getImage(), IMAGE_SUBFOLDER_NAME)))
                .toList();
    }

    @Nullable
    public List<ProductVariant> findAllProductVariantsByProduct(@NotNull final Product product) {
        return this.productVariantRepository.findAllByProduct(product);
    }

    @Nullable
    public ProductVariantCreateRequestDto handleCreateRequestDto(final long productId) {
        if (!this.productRepository.existsById(productId)) {
            return null;
        }

        return new ProductVariantCreateRequestDto();
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
                requestDto.getWeightGrams(),0);

        final var savedProductVariant = productVariantRepository.save(productVariant);
        return savedProductVariant.getId();
    }

    @Nullable
    public ProductVariantUpdateInfoMiddleDto handleCreateMiddleDtoForUpdateInfo(final long id) {
        final var productVariant = this.findProductVariantById(id);
        if (productVariant == null) {
            return null;
        }

        final var imageFilePath = this.imageFileService.getPathString(productVariant.getImage(), IMAGE_SUBFOLDER_NAME);

        final var requestDto = new ProductVariantUpdateRequestDto(
                productVariant.getColor(),
                productVariant.getSize(),
                productVariant.getStockQuantity(),
                productVariant.getPriceCents(),
                productVariant.getWeightGrams());

        return new ProductVariantUpdateInfoMiddleDto(
                productVariant.getProductId(),
                imageFilePath,
                requestDto);
    }

    @Nullable
    public ProductVariantUpdateImageMiddleDto handleCreateMiddleDtoForUpdateImage(final long id) {
        final var productVariant = this.findProductVariantById(id);
        if (productVariant == null) {
            return null;
        }

        final var imageFilePath = this.imageFileService.getPathString(productVariant.getImage(), IMAGE_SUBFOLDER_NAME);
        final var requestDto = new ProductVariantUpdateImageRequestDto();

        return new ProductVariantUpdateImageMiddleDto(
                productVariant.getProductId(),
                imageFilePath,
                requestDto);
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

    public ProductVariant updateInfo(
            final long id,
            @NotNull final ProductVariantUpdateRequestDto requestDto) {
        final var productVariant = findProductVariantById(id);
        if (productVariant == null) {
            return null;
        }

        productVariant.setColor(requestDto.getColor());
        productVariant.setSize(requestDto.getSize());
        productVariant.setPriceCents(requestDto.getPriceCents());
        productVariant.setStockQuantity(requestDto.getStockQuantity());
        productVariant.setWeightGrams(requestDto.getWeightGrams());

        return productVariantRepository.save(productVariant);
    }

    public boolean updateProductVariantImage(
            final ProductVariantUpdateImageRequestDto requestDto,
            long productVariantId) {
        final var productVariant = findProductVariantById(productVariantId);

        if (productVariant == null) {
            return false;
        }

        final var imageFile = requestDto.getImageFile();
        var imageFileName = productVariant.getImage();
        if (!StringUtils.hasText(imageFileName)) {
            imageFileName = imageFileService.handleSaveUploadFile(imageFile, IMAGE_SUBFOLDER_NAME);
        } else {
            imageFileName = imageFileService.handleUpdateUploadFile(imageFileName, imageFile, IMAGE_SUBFOLDER_NAME);
        }
        productVariant.setImage(imageFileName);

        return productVariantRepository.save(productVariant) != null;
    }

    public List<ColorCount> countProductVariantByColor() {
        return productVariantRepository.countByColor();
    }

    public List<SizeCount> countProductVariantBySize() {
        return productVariantRepository.countBySize();
    }

    public void deleteProductVariantById(long id) {
        productVariantRepository.deleteById(id);
    }

    public void deleteProductVariant(@NotNull final ProductVariant productVariant) {
        productVariantRepository.delete(productVariant);
    }

    public List<Long> getProductIdByColor(List<String> listColor) {
        return productVariantRepository.findByColorIn(listColor).stream().map(GetProductId::getProduct_Id).toList();
    }
    public List<Long> getProductIdBySize(List<String> listSize) {
        return productVariantRepository.findBySizeIn(listSize).stream().map(GetProductId::getProduct_Id).toList();
    }
    

    

}