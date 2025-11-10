package vn.uit.clothesshop.area.admin.product.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.category.mapper.CategoryAdminMapper;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.category.domain.Category;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class ProductAdminMapper {
    private final CategoryAdminMapper categoryMapper;
    private final ProductVariantAdminMapper productVariantMapper;
    private final LocalImageStorage storage;

    public ProductAdminMapper(
            final CategoryAdminMapper categoryMapper,
            final ProductVariantAdminMapper productVariantMapper,
            final LocalImageStorage storage) {
        this.categoryMapper = categoryMapper;
        this.productVariantMapper = productVariantMapper;
        this.storage = storage;
    }

    @NonNull
    public Product toEntityOnCreate(
            @NonNull final ProductAdminCreationForm form,
            @NonNull final Category category) {
        return new Product(
                form.getName(),
                form.getShortDesc(),
                form.getDetailDesc(),
                category,
                form.getTargets());
    }

    public void applyUpdateInfo(
            @NonNull final Product entity,
            @NonNull final ProductAdminInfoUpdateForm form,
            @NonNull final Category category) {
        entity.setName(form.getName());
        entity.setShortDesc(form.getShortDesc());
        entity.setDetailDesc(form.getDetailDesc());
        entity.setCategory(category);
        entity.setTargets(form.getTargets());
    }

    @NonNull
    public ProductAdminBasicInfoViewModel toBasicInfo(@NonNull final Product e) {
        return new ProductAdminBasicInfoViewModel(
                e.getId(),
                e.getName(),
                e.getShortDesc());
    }

    @NonNull
    public ProductAdminDetailInfoViewModel toDetailInfo(
            @NonNull final Product e,
            @NonNull final Page<ProductVariant> productVariantPage) {
        final var variantViewModelPage = productVariantPage.map(
                this.productVariantMapper::toBasicInfo);

        return new ProductAdminDetailInfoViewModel(
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

    @NonNull
    public ProductCardViewModel toCard(@NonNull final Product e) {
        return new ProductCardViewModel(
                e.getId(),
                e.getName(),
                e.getShortDesc(),
                this.getPathString(e),
                e.getMinPrice(),
                e.getMaxPrice());
    }

    @NonNull
    public Pair<ProductAdminCreationViewModel, ProductAdminCreationForm> toCreation(
            @NonNull Page<Category> categoryPage) {
        return Pair.of(
                this.toCreationViewModel(categoryPage),
                this.toCreationForm());
    }

    @NonNull
    public ProductAdminCreationViewModel toCreationViewModel(
            @NonNull Page<Category> categoryPage) {
        final var categoryViewModelPage = categoryPage.map(this.categoryMapper::toBasicInfo);
        return new ProductAdminCreationViewModel(categoryViewModelPage);
    }

    @NonNull
    public ProductAdminCreationForm toCreationForm() {
        return new ProductAdminCreationForm();
    }

    @NonNull
    public Pair<ProductAdminInfoUpdateViewModel, ProductAdminInfoUpdateForm> toInfoUpdate(
            @NonNull final Product e,
            @NonNull Page<Category> categoryPage) {
        return Pair.of(
                this.toInfoUpdateViewModel(e, categoryPage),
                this.toInfoUpdateForm(e));
    }

    @NonNull
    public ProductAdminInfoUpdateViewModel toInfoUpdateViewModel(
            @NonNull final Product e,
            @NonNull Page<Category> categoryPage) {
        final var categoryViewModelPage = categoryPage.map(this.categoryMapper::toBasicInfo);
        return new ProductAdminInfoUpdateViewModel(categoryViewModelPage);
    }

    @NonNull
    public ProductAdminInfoUpdateForm toInfoUpdateForm(@NonNull final Product e) {
        return new ProductAdminInfoUpdateForm(
                e.getName(),
                e.getShortDesc(),
                e.getDetailDesc(),
                e.getCategoryId(),
                e.getTargets());
    }

    public String getPathString(@NonNull final Product e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.PRODUCT.sub());
    }
}
