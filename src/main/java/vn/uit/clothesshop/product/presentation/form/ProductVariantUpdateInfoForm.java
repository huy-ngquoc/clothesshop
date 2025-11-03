package vn.uit.clothesshop.product.presentation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import vn.uit.clothesshop.product.domain.ProductVariant;

public class ProductVariantUpdateInfoForm {
    @NotBlank
    @Size(max = ProductVariant.MAX_LENGTH_COLOR)
    private String color = "";

    @NotBlank
    @Size(max = ProductVariant.MAX_LENGTH_SIZE)
    private String size = "";

    @PositiveOrZero
    private int stockQuantity = 0;

    @PositiveOrZero
    private int priceCents = 0;

    @PositiveOrZero
    private int weightGrams = 0;

    public ProductVariantUpdateInfoForm(
            final String color,
            final String size,
            final int stockQuantity,
            final int priceCents,
            final int weightGrams) {
        this.color = color;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.priceCents = priceCents;
        this.weightGrams = weightGrams;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(final int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getPriceCents() {
        return this.priceCents;
    }

    public void setPriceCents(final int priceCents) {
        this.priceCents = priceCents;
    }

    public int getWeightGrams() {
        return this.weightGrams;
    }

    public void setWeightGrams(final int weightGrams) {
        this.weightGrams = weightGrams;
    }

}
