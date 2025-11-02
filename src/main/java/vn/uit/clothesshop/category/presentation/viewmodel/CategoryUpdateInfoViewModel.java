package vn.uit.clothesshop.category.presentation.viewmodel;

import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;

public record CategoryUpdateInfoViewModel(
        String imageFilePath,
        CategoryUpdateInfoForm form) {
}
