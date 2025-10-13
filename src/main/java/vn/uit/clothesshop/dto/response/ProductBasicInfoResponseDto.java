package vn.uit.clothesshop.dto.response;

public class ProductBasicInfoResponseDto {
    private final long id;
    private final String name;
    private final String shortDesc;

    public ProductBasicInfoResponseDto(
            long id,
            String name,
            String shortDesc) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
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
}
