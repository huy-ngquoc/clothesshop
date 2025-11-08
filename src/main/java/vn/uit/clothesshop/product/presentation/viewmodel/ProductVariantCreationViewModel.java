package vn.uit.clothesshop.product.presentation.viewmodel;

public class ProductVariantCreationViewModel {
    private final long productId;

    public ProductVariantCreationViewModel(final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return this.productId;
    }
}
