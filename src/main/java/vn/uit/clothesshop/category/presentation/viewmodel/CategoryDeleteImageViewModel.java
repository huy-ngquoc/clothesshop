package vn.uit.clothesshop.category.presentation.viewmodel;

public final class CategoryDeleteImageViewModel {
    private final String imageFilePath;

    public CategoryDeleteImageViewModel(final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

}
