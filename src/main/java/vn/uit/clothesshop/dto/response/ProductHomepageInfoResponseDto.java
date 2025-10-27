package vn.uit.clothesshop.dto.response;

public class ProductHomepageInfoResponseDto {
    private final long id;
    private final String name;
    private final String shortDesc;
    private final String imageFilePath;
    private final int minPriceCents;
    private final int maxPriceCents;

    public ProductHomepageInfoResponseDto(
            final long id,
            final String name,
            final String shortDesc,
            final String imageFilePath,
            final int minPriceCents,
            final int maxPriceCents) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.imageFilePath = imageFilePath;
        this.minPriceCents = minPriceCents;
        this.maxPriceCents = maxPriceCents;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public int getMinPriceCents() {
        return minPriceCents;
    }

    public int getMaxPriceCents() {
        return maxPriceCents;
    }
}
