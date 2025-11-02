package vn.uit.clothesshop.category.mapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoViewModel;
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
            @NotNull final CategoryUpdateInfoForm form,
            @NotNull final Category entity) {
        entity.setName(StringUtil.trimOrNull(form.getName()));
        entity.setDesc(StringUtil.trimOrNull(form.getDesc()));
    }

    @NotNull
    public CategoryBasicInfoViewModel toBasicInfo(@NotNull Category e) {
        return new CategoryBasicInfoViewModel(
                e.getId(),
                e.getName(),
                e.getDesc());
    }

    @NotNull
    public CategoryDetailInfoViewModel toDetailInfo(@NotNull Category e) {
        return new CategoryDetailInfoViewModel(
                e.getName(),
                e.getDesc(),
                this.getPathString(e),
                e.getAmountOfProduct());
    }

    @NotNull
    public CategoryCardViewModel toCard(@NotNull Category e) {
        return new CategoryCardViewModel(
                e.getId(),
                e.getName(),
                e.getDesc(),
                this.getPathString(e));
    }

    @NotNull
    public CategoryUpdateInfoViewModel toUpdateInfo(@NotNull Category e) {
        final var form = new CategoryUpdateInfoForm(
                e.getName(),
                e.getDesc());

        return new CategoryUpdateInfoViewModel(this.getPathString(e), form);
    }

    public String getPathString(@NotNull final Category e) {
        return this.getPathString(e.getImage());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.CATEGORY.sub());
    }
}
