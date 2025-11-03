package vn.uit.clothesshop.product.mapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductUpdateInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class ProductMapper {
    private final LocalImageStorage storage;

    public ProductMapper(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @NonNull
    public Product toEntityOnCreate(
            @NotNull final ProductCreationForm form,
            @NotNull final Category category) {
        return new Product(
                form.getName(),
                form.getShortDesc(),
                form.getDetailDesc(),
                category,
                form.getTargets());
    }

    public void applyUpdateInfo(
            @NotNull final Product entity,
            @NotNull final ProductUpdateInfoForm form,
            @NotNull final Category category) {
        entity.setName(form.getName());
        entity.setShortDesc(form.getShortDesc());
        entity.setDetailDesc(form.getDetailDesc());
        entity.setCategory(category);
        entity.setTargets(form.getTargets());
    }

    @NotNull
    public ProductBasicInfoViewModel toBasicInfo(@NotNull final Product e) {
        return new ProductBasicInfoViewModel(
                e.getId(),
                e.getName(),
                e.getShortDesc());
    }

    @NotNull
    public ProductDetailInfoViewModel toDetailInfo(@NotNull final Product e) {
        return new ProductDetailInfoViewModel(
                e.getName(),
                e.getShortDesc(),
                e.getDetailDesc(),
                null, // TODO: for now
                e.getMinPrice(),
                e.getMaxPrice(),
                e.getSold(),
                e.getQuantity(),
                this.getPathString(e));
    }

    @NotNull
    public ProductCardViewModel toCard(@NotNull final Product e) {
        return new ProductCardViewModel(
                e.getId(),
                e.getName(),
                e.getShortDesc(),
                this.getPathString(e),
                e.getMinPrice(),
                e.getMaxPrice());
    }

    @NotNull
    public ProductUpdateInfoViewModel toUpdateInfo(@NotNull final Product e) {
        final var form = new ProductUpdateInfoForm(
                e.getName(),
                e.getShortDesc(),
                e.getDetailDesc(),
                e.getCategoryId(),
                e.getTargets());

        return new ProductUpdateInfoViewModel(form);
    }

    public String getPathString(@NotNull final Product e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
