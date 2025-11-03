package vn.uit.clothesshop.product.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductUpdateInfoForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductUpdateInfoViewModel;

public interface ProductService {
    default Page<ProductBasicInfoViewModel> findAllBasic(
            @NotNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable);

    default Page<ProductCardViewModel> findAllForHomepage(
            @NotNull final Pageable pageable) {
        return this.findAllForHomepage(null, pageable);
    }

    Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable);

    Optional<ProductDetailInfoViewModel> findDetailById(final long id);

    long create(@NotNull final ProductCreationForm form);

    Optional<ProductUpdateInfoViewModel> getUpdateInfoById(final long id);

    void updateInfoById(final long id, @NotNull final ProductUpdateInfoForm form);

    void deleteById(final long id);
}
