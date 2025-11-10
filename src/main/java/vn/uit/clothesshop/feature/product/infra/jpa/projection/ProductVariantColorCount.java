package vn.uit.clothesshop.feature.product.infra.jpa.projection;

import jakarta.validation.constraints.NotNull;

public interface ProductVariantColorCount {
    @NotNull
    String getColor();

    long getTotal();
}
