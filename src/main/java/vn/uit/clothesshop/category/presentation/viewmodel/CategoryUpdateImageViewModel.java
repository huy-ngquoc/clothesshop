package vn.uit.clothesshop.category.presentation.viewmodel;

public final class CategoryUpdateImageViewModel {
    private final String imageFilePath;

    public CategoryUpdateImageViewModel(final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

}
