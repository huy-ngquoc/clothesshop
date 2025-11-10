package vn.uit.clothesshop.area.admin.product.presentation.viewmodel;

public class ProductVariantAdminCreationViewModel {
    private final long productId;

    public ProductVariantAdminCreationViewModel(final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return this.productId;
    }
}
