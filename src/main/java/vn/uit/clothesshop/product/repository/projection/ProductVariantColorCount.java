package vn.uit.clothesshop.product.repository.projection;

import jakarta.validation.constraints.NotNull;

public interface ProductVariantColorCount {
    @NotNull
    String getColor();

    long getTotal();
}
