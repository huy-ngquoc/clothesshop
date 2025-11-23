package vn.uit.clothesshop.area.site.homepage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.area.admin.product.presentation.viewmodel.ProductAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.site.homepage.presentation.ProductClientBasicInfoViewModel;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;

public interface ProductClientService {
     default Page<ProductClientBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<ProductClientBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

}
