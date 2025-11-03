package vn.uit.clothesshop.product.presentation.viewmodel;

public class ProductVariantDetailInfoViewModel {
    private final long productId;
    private final String color;
    private final String size;
    private final int stockQuantity;
    private final int priceCents;
    private final int weightGrams;
    private final String imageFilePath;

    public ProductVariantDetailInfoViewModel(
            final long productId,
            final String color,
            final String size,
            final int stockQuantity,
            final int priceCents,
            final int weightGrams,
            final String imageFilePath) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.priceCents = priceCents;
        this.weightGrams = weightGrams;
        this.imageFilePath = imageFilePath;
    }

    public long getProductId() {
        return productId;
    }

    public String getColor() {
        return this.color;
    }

    public String getSize() {
        return this.size;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public int getPriceCents() {
        return this.priceCents;
    }

    public int getWeightGrams() {
        return this.weightGrams;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

}
