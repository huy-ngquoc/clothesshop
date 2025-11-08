package vn.uit.clothesshop.category.mapper;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateImageForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDeleteImageViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateImageViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateInfoViewModel;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.util.StringUtil;

@Component
public class CategoryMapper {
    private final LocalImageStorage storage;

    public CategoryMapper(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @NonNull
    public Category toEntityOnCreate(@NotNull final CategoryCreationForm form) {
        return new Category(
                form.getName(),
                form.getDesc());
    }

    public void applyUpdateInfo(
            @NotNull final Category entity,
            @NotNull final CategoryUpdateInfoForm form) {
        entity.setName(StringUtil.trimOrNull(form.getName()));
        entity.setDesc(StringUtil.trimOrNull(form.getDesc()));
    }

    @NotNull
    public CategoryBasicInfoViewModel toBasicInfo(@NotNull final Category e) {
        return new CategoryBasicInfoViewModel(
                e.getId(),
                e.getName(),
                e.getDesc());
    }

    @NotNull
    public CategoryDetailInfoViewModel toDetailInfo(@NotNull final Category e) {
        return new CategoryDetailInfoViewModel(
                e.getName(),
                e.getDesc(),
                this.getPathString(e),
                e.getAmountOfProduct());
    }

    @NotNull
    public CategoryCardViewModel toCard(@NotNull final Category e) {
        return new CategoryCardViewModel(
                e.getId(),
                e.getName(),
                e.getDesc(),
                this.getPathString(e));
    }

    @NotNull
    public Pair<CategoryUpdateInfoViewModel, CategoryUpdateInfoForm> toUpdateInfo(
            @NotNull final Category e) {
        return Pair.of(
                this.toUpdateInfoViewModel(e),
                this.toUpdateInfoForm(e));
    }

    @NotNull
    public CategoryUpdateInfoViewModel toUpdateInfoViewModel(
            @NotNull final Category e) {
        return new CategoryUpdateInfoViewModel(this.getPathString(e));
    }

    @NotNull
    public CategoryUpdateInfoForm toUpdateInfoForm(@NotNull final Category e) {
        return new CategoryUpdateInfoForm(
                e.getName(),
                e.getDesc());
    }

    @NotNull
    public Pair<CategoryUpdateImageViewModel, CategoryUpdateImageForm> toUpdateImage(
            @NotNull final Category e) {
        return Pair.of(
                this.toUpdateImageViewModel(e),
                this.toUpdateImageForm(e));
    }

    @NotNull
    public CategoryUpdateImageViewModel toUpdateImageViewModel(
            @NotNull final Category e) {
        return new CategoryUpdateImageViewModel(this.getPathString(e));
    }

    @NotNull
    public CategoryUpdateImageForm toUpdateImageForm(@NotNull final Category e) {
        return new CategoryUpdateImageForm();
    }

    @NotNull
    public CategoryDeleteImageViewModel toDeleteImageViewModel(
            @NotNull final Category e) {
        return new CategoryDeleteImageViewModel(this.getPathString(e));
    }

    public String getPathString(@NotNull final Category e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.CATEGORY.sub());
    }
}
