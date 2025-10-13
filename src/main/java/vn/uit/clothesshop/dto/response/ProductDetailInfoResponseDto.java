package vn.uit.clothesshop.dto.response;

public final class ProductDetailInfoResponseDto {
    private final String name;
    private final String shortDesc;
    private final String detailDesc;

    public ProductDetailInfoResponseDto(
            String name,
            String shortDesc,
            String detailDesc) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
    }

    public String getName() {
        return name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

}
