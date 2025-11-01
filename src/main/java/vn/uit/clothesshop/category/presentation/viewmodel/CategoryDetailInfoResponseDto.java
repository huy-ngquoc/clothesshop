package vn.uit.clothesshop.category.presentation.viewmodel;

public final class CategoryDetailInfoResponseDto {
    private final String name;
    private final String desc;
    private final String imageFilePath;
    private final int amountOfProduct;

    public CategoryDetailInfoResponseDto(
            final String name,
            final String desc,
            final String imageFilePath,
            final int amountOfProduct) {
        this.name = name;
        this.desc = desc;
        this.imageFilePath = imageFilePath;
        this.amountOfProduct = amountOfProduct;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public int getAmountOfProduct() {
        return amountOfProduct;
    }
}
