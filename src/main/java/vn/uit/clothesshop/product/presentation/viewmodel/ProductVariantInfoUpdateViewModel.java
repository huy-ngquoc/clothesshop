package vn.uit.clothesshop.product.presentation.viewmodel;

public final class ProductVariantInfoUpdateViewModel {
    private final long productId;
    private final String imageFilePath;

    public ProductVariantInfoUpdateViewModel(
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
