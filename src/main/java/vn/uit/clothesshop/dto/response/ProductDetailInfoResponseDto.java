package vn.uit.clothesshop.dto.response;

public final class ProductDetailInfoResponseDto {
    private final String name;
    private final String shortDesc;
    private final String detailDesc;
    private final long id;
    public ProductDetailInfoResponseDto(
        long id,
            String name,
            String shortDesc,
            String detailDesc) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.id=id;
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

    public long getId() {
        return id;
    }

}
