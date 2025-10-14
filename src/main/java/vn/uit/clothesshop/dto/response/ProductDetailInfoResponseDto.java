package vn.uit.clothesshop.dto.response;

import java.util.ArrayList;
import java.util.List;

public final class ProductDetailInfoResponseDto {
    private final String name;
    private final String shortDesc;
    private final String detailDesc;
    private final List<ProductVariantBasicInfoResponseDto> variantList;

    public ProductDetailInfoResponseDto(
            final String name,
            final String shortDesc,
            final String detailDesc,
            final List<ProductVariantBasicInfoResponseDto> variantList) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.variantList = List.copyOf(variantList);
    }

    public String getName() {
        return this.name;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public String getDetailDesc() {
        return this.detailDesc;
    }

    public List<ProductVariantBasicInfoResponseDto> getVariantList() {
        return new ArrayList<>(this.variantList);
    }

}
