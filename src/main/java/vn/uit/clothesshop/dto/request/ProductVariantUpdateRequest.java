package vn.uit.clothesshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductVariantUpdateRequest {
    private long productVariantId;
    
    @NotBlank
    @Size(max = 10)
    private String color;
    
    @NotBlank
    @Size(max = 10)
    private String size;
    @Positive
    private int priceCents;
    @Positive
    private int stockQuantity;
    @Positive
    private int weightGrams;
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
    public int getPriceCents() {
        return priceCents;
    }
    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public int getWeightGrams() {
        return weightGrams;
    }
    public void setWeightGrams(int weightGrams) {
        this.weightGrams = weightGrams;
    }
    
    public ProductVariantUpdateRequest() {
    }
    public ProductVariantUpdateRequest(long productVariantId,String color, String size, int priceCents, int stockQuantity,
            int weightGrams) {
        this.color = color;
        this.size = size;
        this.priceCents = priceCents;
        this.stockQuantity = stockQuantity;
        this.weightGrams = weightGrams;
        this.productVariantId= productVariantId;
    }
    public long getProductVariantId() {
        return productVariantId;
    }
    public void setProductVariantId(long productVariantId) {
        this.productVariantId = productVariantId;
    }
    

}
