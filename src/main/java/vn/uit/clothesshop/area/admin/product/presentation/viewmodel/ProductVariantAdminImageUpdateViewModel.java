package vn.uit.clothesshop.area.admin.product.presentation.viewmodel;

public final class ProductVariantAdminImageUpdateViewModel {
    private final long productId;
    private final String imageFilePath;

    public ProductVariantAdminImageUpdateViewModel(
            final long productId,
            String imageFilePath) {
        this.productId = productId;
        this.imageFilePath = imageFilePath;
    }

    public long getProductId() {
        return productId;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

}
