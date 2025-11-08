package vn.uit.clothesshop.product.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.mapper.CategoryMapper;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductInfoUpdateForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductInfoUpdateViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCreationViewModel;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class ProductMapper {
    private final CategoryMapper categoryMapper;
    private final ProductVariantMapper productVariantMapper;
    private final LocalImageStorage storage;

    public ProductMapper(
            final CategoryMapper categoryMapper,
            final ProductVariantMapper productVariantMapper,
            final LocalImageStorage storage) {
        this.categoryMapper = categoryMapper;
        this.productVariantMapper = productVariantMapper;
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
            @NotNull final ProductInfoUpdateForm form,
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
    public ProductDetailInfoViewModel toDetailInfo(
            @NotNull final Product e,
            @NonNull final Page<ProductVariant> productVariantPage) {
        final var variantViewModelPage = productVariantPage.map(
                this.productVariantMapper::toBasicInfo);

        return new ProductDetailInfoViewModel(
                e.getName(),
                e.getShortDesc(),
                e.getDetailDesc(),
                variantViewModelPage,
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
    public Pair<ProductCreationViewModel, ProductCreationForm> toCreation(
            @NonNull Page<Category> categoryPage) {
        return Pair.of(
                this.toCreationViewModel(categoryPage),
                this.toCreationForm());
    }

    @NotNull
    public ProductCreationViewModel toCreationViewModel(
            @NonNull Page<Category> categoryPage) {
        final var categoryViewModelPage = categoryPage.map(this.categoryMapper::toBasicInfo);
        return new ProductCreationViewModel(categoryViewModelPage);
    }

    @NotNull
    public ProductCreationForm toCreationForm() {
        return new ProductCreationForm();
    }

    @NotNull
    public Pair<ProductInfoUpdateViewModel, ProductInfoUpdateForm> toInfoUpdate(
            @NotNull final Product e,
            @NonNull Page<Category> categoryPage) {
        return Pair.of(
                this.toInfoUpdateViewModel(e, categoryPage),
                this.toInfoUpdateForm(e));
    }

    @NotNull
    public ProductInfoUpdateViewModel toInfoUpdateViewModel(
            @NotNull final Product e,
            @NonNull Page<Category> categoryPage) {
        final var categoryViewModelPage = categoryPage.map(this.categoryMapper::toBasicInfo);
        return new ProductInfoUpdateViewModel(categoryViewModelPage);
    }

    @NotNull
    public ProductInfoUpdateForm toInfoUpdateForm(@NotNull final Product e) {
        return new ProductInfoUpdateForm(
                e.getName(),
                e.getShortDesc(),
                e.getDetailDesc(),
                e.getCategoryId(),
                e.getTargets());
    }

    public String getPathString(@NotNull final Product e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
