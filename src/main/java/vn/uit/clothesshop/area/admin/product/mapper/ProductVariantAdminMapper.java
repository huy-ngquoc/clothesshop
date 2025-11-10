package vn.uit.clothesshop.area.admin.product.mapper;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDeletionViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminImageUpdateViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class ProductVariantAdminMapper {
    private final LocalImageStorage storage;

    public ProductVariantAdminMapper(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @NonNull
    public ProductVariant toEntityOnCreate(
            @NonNull final ProductVariantAdminCreationForm form,
            @NonNull final Product product) {
        return new ProductVariant(
                product,
                form.getColor(),
                form.getSize(),
                form.getStockQuantity(),
                form.getPriceCents(),
                form.getWeightGrams());
    }

    public void applyUpdateInfo(
            @NonNull final ProductVariant entity,
            @NonNull final ProductVariantAdminInfoUpdateForm form) {
        entity.setColor(form.getColor());
        entity.setSize(form.getSize());
        entity.setStockQuantity(form.getStockQuantity());
        entity.setPriceCents(form.getPriceCents());
        entity.setWeightGrams(form.getWeightGrams());
    }

    @NonNull
    public ProductVariantAdminBasicInfoViewModel toBasicInfo(@NonNull final ProductVariant e) {
        return new ProductVariantAdminBasicInfoViewModel(
                e.getId(),
                e.getColor(),
                e.getSize(),
                e.getStockQuantity(),
                e.getPriceCents(),
                e.getWeightGrams(),
                this.getPathString(e));
    }

    @NonNull
    public ProductVariantAdminDetailInfoViewModel toDetailInfo(@NonNull final ProductVariant e) {
        return new ProductVariantAdminDetailInfoViewModel(
                e.getProductId(),
                e.getColor(),
                e.getSize(),
                e.getStockQuantity(),
                e.getPriceCents(),
                e.getWeightGrams(),
                this.getPathString(e));
    }

    @NonNull
    public Pair<ProductVariantAdminCreationViewModel, ProductVariantAdminCreationForm> toCreation(
            final long productId) {
        return Pair.of(
                this.toCreationViewModel(productId),
                this.toCreationForm());
    }

    @NonNull
    public ProductVariantAdminCreationViewModel toCreationViewModel(final long productId) {
        return new ProductVariantAdminCreationViewModel(productId);
    }

    @NonNull
    public ProductVariantAdminCreationForm toCreationForm() {
        return new ProductVariantAdminCreationForm();
    }

    @NonNull
    public Pair<ProductVariantAdminInfoUpdateViewModel, ProductVariantAdminInfoUpdateForm> toInfoUpdate(
            @NonNull final ProductVariant productVariant) {
        return Pair.of(
                this.toInfoUpdateViewModel(productVariant),
                this.toInfoUpdateForm(productVariant));
    }

    @NonNull
    public ProductVariantAdminInfoUpdateViewModel toInfoUpdateViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantAdminInfoUpdateViewModel(
                productVariant.getProductId(),
                this.getPathString(productVariant));
    }

    @NonNull
    public ProductVariantAdminInfoUpdateForm toInfoUpdateForm(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantAdminInfoUpdateForm(
                productVariant.getColor(),
                productVariant.getSize(),
                productVariant.getStockQuantity(),
                productVariant.getPriceCents(),
                productVariant.getWeightGrams());
    }

    @NonNull
    public Pair<ProductVariantAdminImageUpdateViewModel, ProductVariantAdminImageUpdateForm> toImageUpdate(
            @NonNull final ProductVariant productVariant) {
        return Pair.of(
                this.toImageUpdateViewModel(productVariant),
                this.toImageUpdateForm(productVariant));
    }

    @NonNull
    public ProductVariantAdminImageUpdateViewModel toImageUpdateViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantAdminImageUpdateViewModel(
                productVariant.getProductId(),
                this.getPathString(productVariant));
    }

    @NonNull
    public ProductVariantAdminImageUpdateForm toImageUpdateForm(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantAdminImageUpdateForm();
    }

    @NonNull
    public ProductVariantAdminDeletionViewModel toDeletionViewModel(
            @NonNull final ProductVariant productVariant) {
        return new ProductVariantAdminDeletionViewModel(productVariant.getProductId());
    }

    public String getPathString(@NonNull final ProductVariant e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
