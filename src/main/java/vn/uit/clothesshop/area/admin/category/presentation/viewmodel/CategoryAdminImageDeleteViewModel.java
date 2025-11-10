package vn.uit.clothesshop.area.admin.category.presentation.viewmodel;

public final class CategoryAdminImageDeleteViewModel {
    private final String imageFilePath;

    public CategoryAdminImageDeleteViewModel(final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

}
