package vn.uit.clothesshop.area.admin.product.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;

public interface ProductAdminService {
    default Page<ProductAdminBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    default Page<ProductCardViewModel> findAllForHomepage(
            @NonNull final Pageable pageable) {
        return this.findAllForHomepage(null, pageable);
    }

    Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    default Optional<ProductAdminDetailInfoViewModel> findDetailById(
            final long id,
            @NonNull final Pageable pageable) {
        return this.findDetailById(id, null, pageable);
    }

    Optional<ProductAdminDetailInfoViewModel> findDetailById(
            final long id,
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    Pair<ProductAdminCreationViewModel, ProductAdminCreationForm> findCreation(
            @NonNull final Pageable categoryPageable);

    ProductAdminCreationViewModel findCreationViewModel(
            @NonNull final Pageable categoryPageable);

    long create(@NonNull final ProductAdminCreationForm form);

    Optional<Pair<ProductAdminInfoUpdateViewModel, ProductAdminInfoUpdateForm>> findInfoUpdateById(
            final long id,
            @NonNull final Pageable categoryPageable);

    Optional<ProductAdminInfoUpdateViewModel> findInfoUpdateViewModelById(
            final long id,
            @NonNull final Pageable categoryPageable);

    void updateInfoById(final long id, @NonNull final ProductAdminInfoUpdateForm form);

    void deleteById(final long id);
}
