package vn.uit.clothesshop.area.admin.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.product.mapper.ProductVariantAdminMapper;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDeletionViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminImageUpdateViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantColorCount;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantSizeCount;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
class DefaultProductVariantAdminService implements ProductVariantAdminService {
    private final ProductVariantReadPort variantReadPort;
    private final ProductVariantWritePort variantWritePort;
    private final ProductReadPort productReadPort;
    private final ProductVariantAdminMapper mapper;
    private final LocalImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;

    public DefaultProductVariantAdminService(
            final ProductVariantReadPort variantReadPort,
            final ProductVariantWritePort variantWritePort,
            final ProductVariantAdminMapper mapper,
            final ProductReadPort productReadPort,
            final LocalImageStorage imageStorage,
            final ApplicationEventPublisher eventPublisher) {
        this.variantReadPort = variantReadPort;
        this.variantWritePort = variantWritePort;
        this.mapper = mapper;
        this.productReadPort = productReadPort;
        this.imageStorage = imageStorage;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantColorCount> countProductVariantByColor() {
        return this.variantReadPort.countGroupedByColor();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantSizeCount> countProductVariantBySize() {
        return this.variantReadPort.countGroupedBySize();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getProductIdByColor(final List<String> listColor) {
        return this.variantReadPort.getProductIdByColor(listColor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getProductIdBySize(final List<String> listSize) {
        return this.variantReadPort.getProductIdBySize(listSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductVariantAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable) {
        return this.variantReadPort.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantAdminDetailInfoViewModel> findDetailById(
            final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toDetailInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findProductIdById(final long id) {
        return this.variantReadPort.findProductIdById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pair<ProductVariantAdminCreationViewModel, ProductVariantAdminCreationForm>> findCreationByProductId(
            final long productId) {
        if (!this.productReadPort.existsById(productId)) {
            return Optional.empty();
        }

        return Optional.of(this.mapper.toCreation(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantAdminCreationViewModel> findCreationViewModelByProductId(
            final long productId) {
        if (!this.productReadPort.existsById(productId)) {
            return Optional.empty();
        }

        return Optional.of(this.mapper.toCreationViewModel(productId));
    }

    @Override
    @Transactional
    public long create(
            final long productId,
            @NonNull final ProductVariantAdminCreationForm form) {
        final var product = this.productReadPort.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        final var productVariant = this.mapper.toEntityOnCreate(form, product);
        return this.variantWritePort.save(productVariant).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pair<ProductVariantAdminInfoUpdateViewModel, ProductVariantAdminInfoUpdateForm>> findInfoUpdateById(
            final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toInfoUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantAdminInfoUpdateViewModel> findInfoUpdateViewModelById(final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toInfoUpdateViewModel);
    }

    @Override
    @Transactional
    public void updateInfoById(
            long id,
            @NonNull ProductVariantAdminInfoUpdateForm form) {
        final var productVariant = this.variantReadPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));
        this.mapper.applyUpdateInfo(productVariant, form);
        this.variantWritePort.save(productVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pair<ProductVariantAdminImageUpdateViewModel, ProductVariantAdminImageUpdateForm>> findImageUpdateById(
            final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toImageUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantAdminImageUpdateViewModel> findImageUpdateViewModelById(final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toImageUpdateViewModel);
    }

    @Override
    @Transactional
    public void updateImageById(
            final long id,
            @NonNull final ProductVariantAdminImageUpdateForm form) {
        final var productVariant = this.variantReadPort.findById(id)
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

        this.variantWritePort.save(productVariant);
    }

    @Override
    @Transactional
    public void deleteImageById(final long id) {
        final var productVariant = this.variantReadPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));

        final var imageFile = productVariant.getImage();
        if (!StringUtils.hasText(imageFile)) {
            return;
        }

        productVariant.setImage(null);
        eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.PRODUCT_VARIANT));

        this.variantWritePort.save(productVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantAdminDeletionViewModel> findDeletionViewModelById(final long id) {
        return this.variantReadPort.findById(id).map(this.mapper::toDeletionViewModel);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var productVariant = this.variantReadPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Variant not found"));
        final var imageFile = productVariant.getImage();
        if (!StringUtils.hasText(imageFile)) {
            eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));
        }

        this.variantWritePort.delete(productVariant);
    }

}
