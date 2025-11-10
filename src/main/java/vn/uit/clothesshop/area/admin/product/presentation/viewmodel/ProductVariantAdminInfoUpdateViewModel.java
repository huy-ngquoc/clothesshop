package vn.uit.clothesshop.area.admin.product.presentation.viewmodel;

public final class ProductVariantAdminInfoUpdateViewModel {
    private final long productId;
    private final String imageFilePath;

    public ProductVariantAdminInfoUpdateViewModel(
            final long productId,
            final String imageFilePath) {
        this.productId = productId;
        this.imageFilePath = imageFilePath;
    }

    public long getProductId() {
        return productId;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

}
