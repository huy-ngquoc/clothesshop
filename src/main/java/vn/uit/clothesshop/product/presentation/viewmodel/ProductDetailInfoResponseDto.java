package vn.uit.clothesshop.product.presentation.viewmodel;

import java.util.ArrayList;
import java.util.List;

public final class ProductDetailInfoResponseDto {
    private final String name;
    private final String shortDesc;
    private final String detailDesc;
    private final List<ProductVariantBasicInfoResponseDto> variantList;
    private final int minPrice;
    private final int maxPrice;
    private final int sold;
    private final int quantity;
    private final String image;

    public ProductDetailInfoResponseDto(
            final String name,
            final String shortDesc,
            final String detailDesc,
            final List<ProductVariantBasicInfoResponseDto> variantList, int minPrice, int maxPrice, int sold,
            int quantity, final String image) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.variantList = List.copyOf(variantList);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sold = sold;
        this.quantity = quantity;
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getSold() {
        return sold;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return this.name;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public String getDetailDesc() {
        return this.detailDesc;
    }

    public List<ProductVariantBasicInfoResponseDto> getVariantList() {
        return new ArrayList<>(this.variantList);
    }

}
