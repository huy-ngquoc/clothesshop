package vn.uit.clothesshop.feature.product.infra.jpa.projection;

import jakarta.validation.constraints.NotNull;

public interface ProductVariantSizeCount {
    @NotNull
    String getSize();

    long getTotal();
}
