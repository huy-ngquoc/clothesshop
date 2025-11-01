package vn.uit.clothesshop.category.presentation.viewmodel;

import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoRequestDto;

public record CategoryUpdateInfoMiddleDto(
        String imageFilePath,
        CategoryUpdateInfoRequestDto requestDto) {
}
