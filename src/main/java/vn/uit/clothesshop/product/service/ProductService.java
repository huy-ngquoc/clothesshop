package vn.uit.clothesshop.product.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductInfoUpdateForm;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductBasicInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCardViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductCreationViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoViewModel;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductInfoUpdateViewModel;

public interface ProductService {
    default Page<ProductBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    default Page<ProductCardViewModel> findAllForHomepage(
            @NonNull final Pageable pageable) {
        return this.findAllForHomepage(null, pageable);
    }

    Page<ProductCardViewModel> findAllForHomepage(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    default Optional<ProductDetailInfoViewModel> findDetailById(
            final long id,
            @NonNull final Pageable pageable) {
        return this.findDetailById(id, null, pageable);
    }

    Optional<ProductDetailInfoViewModel> findDetailById(
            final long id,
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    Pair<ProductCreationViewModel, ProductCreationForm> findCreation(
            @NonNull final Pageable categoryPageable);

    ProductCreationViewModel findCreationViewModel(
            @NonNull final Pageable categoryPageable);

    long create(@NonNull final ProductCreationForm form);

    Optional<Pair<ProductInfoUpdateViewModel, ProductInfoUpdateForm>> findInfoUpdateById(
            final long id,
            @NonNull final Pageable categoryPageable);

    Optional<ProductInfoUpdateViewModel> findInfoUpdateViewModelById(
            final long id,
            @NonNull final Pageable categoryPageable);

    void updateInfoById(final long id, @NonNull final ProductInfoUpdateForm form);

    void deleteById(final long id);
}
