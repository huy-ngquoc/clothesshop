package vn.uit.clothesshop.category.presentation.viewmodel;

import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;

public final class CategoryUpdateInfoViewModel {
    private final String imageFilePath;
    private final CategoryUpdateInfoForm form;

    public CategoryUpdateInfoViewModel(
            final String imageFilePath,
            final CategoryUpdateInfoForm form) {
        this.imageFilePath = imageFilePath;
        this.form = form;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

    public CategoryUpdateInfoForm getForm() {
        return this.form;
    }

}
