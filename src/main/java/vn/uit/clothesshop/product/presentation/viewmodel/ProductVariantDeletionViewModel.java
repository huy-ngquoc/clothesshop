package vn.uit.clothesshop.product.presentation.viewmodel;

public final class ProductVariantDeletionViewModel {
    private final long productId;

    public ProductVariantDeletionViewModel(
            final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
