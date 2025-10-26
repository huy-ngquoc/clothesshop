package vn.uit.clothesshop.repository.projection;

import jakarta.validation.constraints.NotNull;

public interface ProductVariantSizeCount {
    @NotNull
    String getSize();

    long getTotal();
}
