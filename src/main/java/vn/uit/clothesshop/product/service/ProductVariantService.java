package vn.uit.clothesshop.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantCreationInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateImageViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantUpdateInfoViewModel;
import vn.uit.clothesshop.product.repository.projection.ProductVariantColorCount;
import vn.uit.clothesshop.product.repository.projection.ProductVariantSizeCount;

public interface ProductVariantService {
    List<ProductVariantColorCount> countProductVariantByColor();

    List<ProductVariantSizeCount> countProductVariantBySize();

    List<Long> getProductIdByColor(final List<String> listColor);

    List<Long> getProductIdBySize(final List<String> listSize);

    default Page<ProductVariantBasicInfoViewModel> findAllBasic(
            @NotNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductVariantBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<ProductVariant> spec,
            @NotNull final Pageable pageable);

    Optional<ProductVariantDetailInfoViewModel> findDetailById(final long id);

    boolean existsById(final long id);

    Optional<Long> findProductIdById(final long id);

    Optional<ProductVariantCreationInfoViewModel> getCreationInfo(
            final long productId);

    long create(
            final long productId,
            @NotNull final ProductVariantCreationForm form);

    Optional<ProductVariantUpdateInfoViewModel> getUpdateInfoById(final long id);

    void updateInfoById(final long id, @NotNull final ProductVariantUpdateInfoForm form);

    Optional<ProductVariantUpdateImageViewModel> getUpdateImageById(final long id);

    void updateImageById(final long id, @NotNull final ProductVariantUpdateImageForm form);

    void deleteImageById(final long id);

    void deleteById(final long id);
}
