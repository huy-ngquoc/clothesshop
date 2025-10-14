package vn.uit.clothesshop.dto.middle;

import vn.uit.clothesshop.dto.request.ProductVariantUpdateRequestDto;

public final class ProductVariantUpdateInfoMiddleDto {
    private final String avatarFilePath;
    private final ProductVariantUpdateRequestDto requestDto;

    public ProductVariantUpdateInfoMiddleDto(
            final String avatarFilePath,
            final ProductVariantUpdateRequestDto requestDto) {
        this.avatarFilePath = avatarFilePath;
        this.requestDto = requestDto;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public ProductVariantUpdateRequestDto getRequestDto() {
        return requestDto;
    }

}
