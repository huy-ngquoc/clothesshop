package vn.uit.clothesshop.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.product.domain.ProductAccess;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.mapper.ProductVariantMapper;
import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantCreationInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateImageViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateInfoViewModel;
import vn.uit.clothesshop.product.repository.ProductVariantRepository;
import vn.uit.clothesshop.product.repository.projection.ProductVariantColorCount;
import vn.uit.clothesshop.product.repository.projection.ProductVariantSizeCount;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
@Transactional(readOnly = true)
@Slf4j
class JpaProductVariantService implements ProductVariantService {
    private final ProductVariantRepository repository;
    private final ProductVariantMapper mapper;
    private final ProductAccess productAccess;
    private final LocalImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;

    public JpaProductVariantService(
            final ProductVariantRepository repository,
            final ProductVariantMapper mapper,
            final ProductAccess productAccess,
            final LocalImageStorage imageFileService,
            final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.productAccess = productAccess;
        this.imageStorage = imageFileService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<ProductVariantColorCount> countProductVariantByColor() {
        return repository.countByColor();
    }

    @Override
    public List<ProductVariantSizeCount> countProductVariantBySize() {
        return repository.countBySize();
    }

    @Override
    public List<Long> getProductIdByColor(final List<String> listColor) {
        return repository.findByColorIn(listColor);
    }

    @Override
    public List<Long> getProductIdBySize(final List<String> listSize) {
        return repository.findBySizeIn(listSize);
    }

    @Override
    public Page<ProductVariantBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<ProductVariant> spec,
            @NotNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public Optional<ProductVariantDetailInfoViewModel> findDetailById(
            final long id) {
        return this.repository.findById(id).map(this.mapper::toDetailInfo);
    }

    @Override
    public boolean existsById(final long id) {
        return this.repository.existsById(id);
    }

    @Override
    public Optional<Long> findProductIdById(final long id) {
        return this.repository.findProductIdById(id);
    }

    @Override
    public Optional<ProductVariantCreationInfoViewModel> getCreationInfo(
            final long productId) {
        if (!this.productAccess.existsById(productId)) {
            return Optional.empty();
        }

        final var form = new ProductVariantCreationForm();
        return Optional.of(new ProductVariantCreationInfoViewModel(form));
    }

    @Override
    @Transactional
    public long create(
            final long productId,
            @NotNull final ProductVariantCreationForm form) {
        final var product = this.productAccess.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        final var productVariant = this.mapper.toEntityOnCreate(form, product);
        return this.repository.save(productVariant).getId();
    }

    @Override
    public Optional<ProductVariantUpdateInfoViewModel> getUpdateInfoById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Override
    @Transactional
    public void updateInfoById(
            long id,
            @NotNull ProductVariantUpdateInfoForm form) {
        final var productVariant = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));
        this.mapper.applyUpdateInfo(productVariant, form);
    }

    @Override
    public Optional<ProductVariantUpdateImageViewModel> getUpdateImageById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateImage);
    }

    @Override
    @Transactional
    public void updateImageById(final long id, @NotNull final ProductVariantUpdateImageForm form) {
        final var productVariant = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));

        final var newImageFileName = this.imageStorage.handleSaveUploadFile(
                form.getImageFile(),
                ImageFolder.PRODUCT_VARIANT.sub());
        if (!StringUtils.hasText(newImageFileName)) {
            return;
        }

        final var oldImageFileName = productVariant.getImage();
        productVariant.setImage(newImageFileName);
        this.eventPublisher.publishEvent(
                new ImageReplaced(
                        oldImageFileName,
                        newImageFileName,
                        ImageFolder.PRODUCT_VARIANT));

    }

    @Override
    @Transactional
    public void deleteImageById(final long id) {
        final var productVariant = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));

        final var imageFile = productVariant.getImage();
        if (!StringUtils.hasText(imageFile)) {
            return;
        }

        productVariant.setImage(null);
        eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.PRODUCT_VARIANT));
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var productVariant = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));
        final var imageFile = productVariant.getImage();
        if (!StringUtils.hasText(imageFile)) {
            eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));
        }

        this.repository.delete(productVariant);
    }

}
