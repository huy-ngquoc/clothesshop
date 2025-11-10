package vn.uit.clothesshop.area.admin.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminCreationViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDeletionViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminImageUpdateViewModel;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductVariantAdminInfoUpdateViewModel;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantColorCount;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantSizeCount;

public interface ProductVariantAdminService {
    List<ProductVariantColorCount> countProductVariantByColor();

    List<ProductVariantSizeCount> countProductVariantBySize();

    List<Long> getProductIdByColor(final List<String> listColor);

    List<Long> getProductIdBySize(final List<String> listSize);

    default Page<ProductVariantAdminBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductVariantAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    Optional<ProductVariantAdminDetailInfoViewModel> findDetailById(final long id);

    Optional<Long> findProductIdById(final long id);

    Optional<Pair<ProductVariantAdminCreationViewModel, ProductVariantAdminCreationForm>> findCreationByProductId(
            final long productId);

    Optional<ProductVariantAdminCreationViewModel> findCreationViewModelByProductId(
            final long productId);

    long create(
            final long productId,
            @NonNull final ProductVariantAdminCreationForm form);

    Optional<Pair<ProductVariantAdminInfoUpdateViewModel, ProductVariantAdminInfoUpdateForm>> findInfoUpdateById(
            final long id);

    Optional<Pair<ProductVariantAdminImageUpdateViewModel, ProductVariantAdminImageUpdateForm>> findImageUpdateById(
            final long id);

    Optional<ProductVariantAdminInfoUpdateViewModel> findInfoUpdateViewModelById(final long id);

    void updateInfoById(
            final long id,
            @NonNull final ProductVariantAdminInfoUpdateForm form);

    Optional<ProductVariantAdminImageUpdateViewModel> findImageUpdateViewModelById(final long id);

    void updateImageById(
            final long id,
            @NonNull final ProductVariantAdminImageUpdateForm form);

    void deleteImageById(final long id);

    Optional<ProductVariantAdminDeletionViewModel> findDeletionViewModelById(final long id);

    void deleteById(final long id);
}
