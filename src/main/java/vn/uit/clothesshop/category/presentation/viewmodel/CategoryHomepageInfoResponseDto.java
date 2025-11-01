package vn.uit.clothesshop.category.presentation.viewmodel;

public final class CategoryHomepageInfoResponseDto {
    private final long id;
    private final String name;
    private final String desc;
    private final String imageFilePath;

    public CategoryHomepageInfoResponseDto(
            final long id,
            final String name,
            final String desc,
            final String imageFilePath) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imageFilePath = imageFilePath;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

}
