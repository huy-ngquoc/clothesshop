package vn.uit.clothesshop.dto.response;

public final class ProductVariantBasicInfoResponseDto {
    private final long id;
    private final String color;
    private final String size;
    private final int stockQuantity;
    private final int weightGrams;

    public ProductVariantBasicInfoResponseDto(
            final long id,
            final String color,
            final String size,
            final int stockQuantity,
            final int weightGrams) {
        this.id = id;
        this.color = color;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.weightGrams = weightGrams;
    }

    public long getId() {
        return this.id;
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

    public int getWeightGrams() {
        return this.weightGrams;
    }

}
