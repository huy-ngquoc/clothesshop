package vn.uit.clothesshop.area.admin.category.mapper;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminCreationForm;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminImageDeleteViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminImageUpdateViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminInfoUpdateViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.feature.category.domain.Category;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.util.StringUtil;

@Component
public class CategoryAdminMapper {
    private final LocalImageStorage storage;

    public CategoryAdminMapper(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @NonNull
    public Category toEntityOnCreate(@NonNull final CategoryAdminCreationForm form) {
        return new Category(
                form.getName(),
                form.getDesc());
    }

    public void applyUpdateInfo(
            @NonNull final Category entity,
            @NonNull final CategoryAdminInfoUpdateForm form) {
        entity.setName(StringUtil.trimOrNull(form.getName()));
        entity.setDesc(StringUtil.trimOrNull(form.getDesc()));
    }

    @NonNull
    public CategoryAdminBasicInfoViewModel toBasicInfo(@NonNull final Category e) {
        return new CategoryAdminBasicInfoViewModel(
                e.getId(),
                e.getName(),
                e.getDesc());
    }

    @NonNull
    public CategoryAdminDetailInfoViewModel toDetailInfo(@NonNull final Category e) {
        return new CategoryAdminDetailInfoViewModel(
                e.getName(),
                e.getDesc(),
                this.getImageFilePathString(e),
                e.getAmountOfProduct());
    }

    @NonNull
    public CategoryCardViewModel toCard(@NonNull final Category e) {
        return new CategoryCardViewModel(
                e.getId(),
                e.getName(),
                e.getDesc(),
                this.getImageFilePathString(e));
    }

    @NonNull
    public Pair<CategoryAdminInfoUpdateViewModel, CategoryAdminInfoUpdateForm> toUpdateInfo(
            @NonNull final Category e) {
        return Pair.of(
                this.toUpdateInfoViewModel(e),
                this.toUpdateInfoForm(e));
    }

    @NonNull
    public CategoryAdminInfoUpdateViewModel toUpdateInfoViewModel(
            @NonNull final Category e) {
        return new CategoryAdminInfoUpdateViewModel(this.getImageFilePathString(e));
    }

    @NonNull
    public CategoryAdminInfoUpdateForm toUpdateInfoForm(@NonNull final Category e) {
        return new CategoryAdminInfoUpdateForm(
                e.getName(),
                e.getDesc());
    }

    @NonNull
    public Pair<CategoryAdminImageUpdateViewModel, CategoryAdminImageUpdateForm> toUpdateImage(
            @NonNull final Category e) {
        return Pair.of(
                this.toUpdateImageViewModel(e),
                this.toUpdateImageForm(e));
    }

    @NonNull
    public CategoryAdminImageUpdateViewModel toUpdateImageViewModel(
            @NonNull final Category e) {
        return new CategoryAdminImageUpdateViewModel(this.getImageFilePathString(e));
    }

    @NonNull
    public CategoryAdminImageUpdateForm toUpdateImageForm(@NonNull final Category e) {
        return new CategoryAdminImageUpdateForm();
    }

    @NonNull
    public CategoryAdminImageDeleteViewModel toDeleteImageViewModel(
            @NonNull final Category e) {
        return new CategoryAdminImageDeleteViewModel(this.getImageFilePathString(e));
    }

    private String getImageFilePathString(@NonNull final Category e) {
        return this.getImageFilePathString(e.getImage());
    }

    private String getImageFilePathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.CATEGORY.sub());
    }
}
