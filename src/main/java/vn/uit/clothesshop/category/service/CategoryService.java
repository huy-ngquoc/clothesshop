package vn.uit.clothesshop.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateInfoViewModel;

public interface CategoryService {
    Page<CategoryBasicInfoViewModel> findAllBasic(@NonNull final Pageable pageable);

    List<CategoryCardViewModel> findRandomForHomepage(int size);

    Optional<CategoryDetailInfoViewModel> findDetailById(final long id);

    Optional<String> getPathStringById(final long id);

    long create(@NotNull final CategoryCreationForm form);

    Optional<CategoryUpdateInfoViewModel> getUpdateInfoViewModel(final long id);

    boolean updateInfo(final long id, @NotNull final CategoryUpdateInfoForm form);

    boolean updateImage(final long id, final MultipartFile file);

    boolean deleteImage(final long id);

    void deleteById(final long id);
}
