package vn.uit.clothesshop.area.admin.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

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

public interface CategoryAdminService {
    default Page<CategoryAdminBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<CategoryAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable);

    List<CategoryCardViewModel> findRandomForHomepage(int size);

    Optional<CategoryAdminDetailInfoViewModel> findDetailById(final long id);

    long create(@NonNull final CategoryAdminCreationForm form);

    Optional<Pair<CategoryAdminInfoUpdateViewModel, CategoryAdminInfoUpdateForm>> findInfoUpdateById(final long id);

    Optional<CategoryAdminInfoUpdateViewModel> findInfoUpdateViewModelById(final long id);

    void updateInfoById(final long id, @NonNull final CategoryAdminInfoUpdateForm form);

    Optional<Pair<CategoryAdminImageUpdateViewModel, CategoryAdminImageUpdateForm>> findImageUpdateById(final long id);

    Optional<CategoryAdminImageUpdateViewModel> findImageUpdateViewModelById(final long id);

    boolean updateImageById(final long id, final CategoryAdminImageUpdateForm form);

    Optional<CategoryAdminImageDeleteViewModel> findImageDeleteViewModelById(final long id);

    void deleteImageById(final long id);

    void deleteById(final long id);
}
