package vn.uit.clothesshop.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantCreationViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDeletionViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantImageUpdateViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantInfoUpdateViewModel;
import vn.uit.clothesshop.product.repository.projection.ProductVariantColorCount;
import vn.uit.clothesshop.product.repository.projection.ProductVariantSizeCount;

public interface ProductVariantService {
    List<ProductVariantColorCount> countProductVariantByColor();

    List<ProductVariantSizeCount> countProductVariantBySize();

    List<Long> getProductIdByColor(final List<String> listColor);

    List<Long> getProductIdBySize(final List<String> listSize);

    default Page<ProductVariantBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductVariantBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    Optional<ProductVariantDetailInfoViewModel> findDetailById(final long id);

    boolean existsById(final long id);

    Optional<Long> findProductIdById(final long id);

    Optional<Pair<ProductVariantCreationViewModel, ProductVariantCreationForm>> findCreationByProductId(
            final long productId);

    Optional<ProductVariantCreationViewModel> findCreationViewModelByProductId(
            final long productId);

    long create(
            final long productId,
            @NonNull final ProductVariantCreationForm form);

    Optional<Pair<ProductVariantInfoUpdateViewModel, ProductVariantUpdateInfoForm>> findInfoUpdateById(
            final long id);

    Optional<Pair<ProductVariantImageUpdateViewModel, ProductVariantUpdateImageForm>> findImageUpdateById(
            final long id);

    Optional<ProductVariantInfoUpdateViewModel> findInfoUpdateViewModelById(final long id);

    void updateInfoById(
            final long id,
            @NonNull final ProductVariantUpdateInfoForm form);

    Optional<ProductVariantImageUpdateViewModel> findImageUpdateViewModelById(final long id);

    void updateImageById(
            final long id,
            @NonNull final ProductVariantUpdateImageForm form);

    void deleteImageById(final long id);

    Optional<ProductVariantDeletionViewModel> findDeletionViewModelById(final long id);

    void deleteById(final long id);
}
