package vn.uit.clothesshop.product.presentation.viewmodel;

import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateRequestDto;

public final class ProductVariantUpdateInfoMiddleDto {
    private final long productId;
    private final String avatarFilePath;
    private final ProductVariantUpdateRequestDto requestDto;

    public ProductVariantUpdateInfoMiddleDto(
            final long productId,
            final String avatarFilePath,
            final ProductVariantUpdateRequestDto requestDto) {
        this.productId = productId;
        this.avatarFilePath = avatarFilePath;
        this.requestDto = requestDto;
    }

    public long getProductId() {
        return productId;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public ProductVariantUpdateRequestDto getRequestDto() {
        return requestDto;
    }

}
