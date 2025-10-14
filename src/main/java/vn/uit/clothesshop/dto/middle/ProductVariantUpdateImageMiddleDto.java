package vn.uit.clothesshop.dto.middle;

import vn.uit.clothesshop.dto.request.ProductVariantUpdateImageRequestDto;

public final class ProductVariantUpdateImageMiddleDto {
    private final long productId;
    private final String imageFilePath;
    private final ProductVariantUpdateImageRequestDto requestDto;

    public ProductVariantUpdateImageMiddleDto(
            long productId,
            String imageFilePath,
            ProductVariantUpdateImageRequestDto requestDto) {
        this.productId = productId;
        this.imageFilePath = imageFilePath;
        this.requestDto = requestDto;
    }

    public long getProductId() {
        return productId;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public ProductVariantUpdateImageRequestDto getRequestDto() {
        return requestDto;
    }

}
