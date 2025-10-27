package vn.uit.clothesshop.dto.middle;

import vn.uit.clothesshop.dto.request.CategoryUpdateInfoRequestDto;

public record CategoryUpdateInfoMiddleDto(
        String imageFilePath,
        CategoryUpdateInfoRequestDto requestDto) {
}
