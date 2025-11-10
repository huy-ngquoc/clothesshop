package vn.uit.clothesshop.area.admin.category.presentation.viewmodel;

public final class CategoryAdminImageUpdateViewModel {
    private final String imageFilePath;

    public CategoryAdminImageUpdateViewModel(final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

}
