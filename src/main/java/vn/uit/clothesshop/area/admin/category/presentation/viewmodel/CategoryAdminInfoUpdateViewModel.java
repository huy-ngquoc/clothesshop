package vn.uit.clothesshop.area.admin.category.presentation.viewmodel;

public final class CategoryAdminInfoUpdateViewModel {
    private final String imageFilePath;

    public CategoryAdminInfoUpdateViewModel(
            final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

}
