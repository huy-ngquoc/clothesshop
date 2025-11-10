package vn.uit.clothesshop.area.admin.product.presentation.viewmodel;

public final class ProductVariantAdminDeletionViewModel {
    private final long productId;

    public ProductVariantAdminDeletionViewModel(
            final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
