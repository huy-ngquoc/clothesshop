package vn.uit.clothesshop.product.presentation.viewmodel;

public final class ProductVariantImageUpdateViewModel {
    private final long productId;
    private final String imageFilePath;

    public ProductVariantImageUpdateViewModel(
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
