package vn.uit.clothesshop.product.mapper;

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
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateImageViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateInfoViewModel;
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
    public ProductVariantUpdateInfoViewModel toUpdateInfo(@NotNull final ProductVariant e) {
        final var form = new ProductVariantUpdateInfoForm(
                e.getColor(),
                e.getSize(),
                e.getStockQuantity(),
                e.getPriceCents(),
                e.getWeightGrams());

        return new ProductVariantUpdateInfoViewModel(
                e.getProductId(),
                this.getPathString(e),
                form);
    }

    @NotNull
    public ProductVariantUpdateImageViewModel toUpdateImage(@NotNull final ProductVariant e) {
        final var form = new ProductVariantUpdateImageForm();

        return new ProductVariantUpdateImageViewModel(
                e.getProductId(),
                this.getPathString(e),
                form);
    }

    public String getPathString(@NotNull final ProductVariant e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
