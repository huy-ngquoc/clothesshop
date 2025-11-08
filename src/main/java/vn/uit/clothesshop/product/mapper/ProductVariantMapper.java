package vn.uit.clothesshop.product.mapper;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantCreationViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDeletionViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantImageUpdateViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantInfoUpdateViewModel;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class ProductVariantMapper {
    private final LocalImageStorage storage;

    public ProductVariantMapper(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @NonNull
    public ProductVariant toEntityOnCreate(
            @NotNull final ProductVariantCreationForm form,
            @NotNull final Product product) {
        return new ProductVariant(
                product,
                form.getColor(),
                form.getSize(),
                form.getStockQuantity(),
                form.getPriceCents(),
                form.getWeightGrams());
    }

    public void applyUpdateInfo(
            @NotNull final ProductVariant entity,
            @NotNull final ProductVariantUpdateInfoForm form) {
        entity.setColor(form.getColor());
        entity.setSize(form.getSize());
        entity.setStockQuantity(form.getStockQuantity());
        entity.setPriceCents(form.getPriceCents());
        entity.setWeightGrams(form.getWeightGrams());
    }

    @NotNull
    public ProductVariantBasicInfoViewModel toBasicInfo(@NotNull final ProductVariant e) {
        return new ProductVariantBasicInfoViewModel(
                e.getId(),
                e.getColor(),
                e.getSize(),
                e.getStockQuantity(),
                e.getPriceCents(),
                e.getWeightGrams(),
                this.getPathString(e));
    }

    @NotNull
    public ProductVariantDetailInfoViewModel toDetailInfo(@NotNull final ProductVariant e) {
        return new ProductVariantDetailInfoViewModel(
                e.getProductId(),
                e.getColor(),
                e.getSize(),
                e.getStockQuantity(),
                e.getPriceCents(),
                e.getWeightGrams(),
                this.getPathString(e));
    }

    @NotNull
    public Pair<ProductVariantCreationViewModel, ProductVariantCreationForm> toCreation(
            @NotNull final long productId) {
        return Pair.of(
                this.toCreationViewModel(productId),
                this.toCreationForm());
    }

    @NotNull
    public ProductVariantCreationViewModel toCreationViewModel(@NotNull final long productId) {
        return new ProductVariantCreationViewModel(productId);
    }

    @NotNull
    public ProductVariantCreationForm toCreationForm() {
        return new ProductVariantCreationForm();
    }

    @NonNull
    public Pair<ProductVariantInfoUpdateViewModel, ProductVariantUpdateInfoForm> toInfoUpdate(
            @NonNull final ProductVariant productVariant) {
        return Pair.of(
                this.toInfoUpdateViewModel(productVariant),
                this.toInfoUpdateForm(productVariant));
    }

    @NonNull
    public ProductVariantInfoUpdateViewModel toInfoUpdateViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantInfoUpdateViewModel(
                productVariant.getProductId(),
                this.getPathString(productVariant));
    }

    @NonNull
    public ProductVariantUpdateInfoForm toInfoUpdateForm(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantUpdateInfoForm(
                productVariant.getColor(),
                productVariant.getSize(),
                productVariant.getStockQuantity(),
                productVariant.getPriceCents(),
                productVariant.getWeightGrams());
    }

    @NonNull
    public Pair<ProductVariantImageUpdateViewModel, ProductVariantUpdateImageForm> toImageUpdate(
            @NonNull final ProductVariant productVariant) {
        return Pair.of(
                this.toImageUpdateViewModel(productVariant),
                this.toImageUpdateForm(productVariant));
    }

    @NonNull
    public ProductVariantImageUpdateViewModel toImageUpdateViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantImageUpdateViewModel(
                productVariant.getProductId(),
                this.getPathString(productVariant));
    }

    @NonNull
    public ProductVariantUpdateImageForm toImageUpdateForm(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantUpdateImageForm();
    }

    @NonNull
    public ProductVariantDeletionViewModel toDeletionViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantDeletionViewModel(productVariant.getProductId());
    }

    public String getPathString(@NotNull final ProductVariant e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
