package vn.uit.clothesshop.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

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

public interface CategoryService {
    default Page<CategoryBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<CategoryBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable);

    List<CategoryCardViewModel> findRandomForHomepage(int size);

    Optional<CategoryDetailInfoViewModel> findDetailById(final long id);

    long create(@NotNull final CategoryCreationForm form);

    Optional<Pair<CategoryUpdateInfoViewModel, CategoryUpdateInfoForm>> findInfoUpdateById(final long id);

    Optional<CategoryUpdateInfoViewModel> findInfoUpdateViewModelById(final long id);

    void updateInfoById(final long id, @NotNull final CategoryUpdateInfoForm form);

    Optional<Pair<CategoryUpdateImageViewModel, CategoryUpdateImageForm>> findImageUpdateById(final long id);

    Optional<CategoryUpdateImageViewModel> findImageUpdateViewModelById(final long id);

    boolean updateImageById(final long id, final CategoryUpdateImageForm form);

    Optional<CategoryDeleteImageViewModel> findImageDeleteViewModelById(final long id);

    void deleteImageById(final long id);

    void deleteById(final long id);
}
