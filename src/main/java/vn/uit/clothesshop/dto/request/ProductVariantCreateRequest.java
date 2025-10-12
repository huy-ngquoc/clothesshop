package vn.uit.clothesshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductVariantCreateRequest {
     @NotBlank
    @Size(max = 10)
    private String color = "";
    @PositiveOrZero
    private long productId;
    @NotBlank
    @Size(max = 10)
    private String size = "";

    @PositiveOrZero
    private int stockQuantity = 0;

    @PositiveOrZero
    private int priceCents = 0;

    @PositiveOrZero
    private int weightGrams = 0;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(int weightGrams) {
        this.weightGrams = weightGrams;
    }

    public ProductVariantCreateRequest() {
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public ProductVariantCreateRequest(@NotBlank @Size(max = 10) String color, @NotBlank @Size(max = 10) String size,
            @PositiveOrZero int stockQuantity, @PositiveOrZero int priceCents, @PositiveOrZero int weightGrams, long productId) {
        this.color = color;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.priceCents = priceCents;
        this.weightGrams = weightGrams;
        this.productId= productId;
    }
    
}
