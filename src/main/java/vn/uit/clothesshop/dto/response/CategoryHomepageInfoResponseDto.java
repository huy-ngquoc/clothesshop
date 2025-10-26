package vn.uit.clothesshop.dto.response;

public final class CategoryHomepageInfoResponseDto {
    private final long id;
    private final String name;
    private final String imageFilePath;

    public CategoryHomepageInfoResponseDto(
            final long id,
            final String name,
            final String imageFilePath) {
        this.id = id;
        this.name = name;
        this.imageFilePath = imageFilePath;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

}
