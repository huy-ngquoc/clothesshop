package vn.uit.clothesshop.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.uit.clothesshop.domain.ETarget;
import vn.uit.clothesshop.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDto {
    @NotBlank
    @Size(min = Product.MIN_LENGTH_NAME, max = Product.MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(min = Product.MIN_LENGTH_SHORT_DESC, max = Product.MAX_LENGTH_SHORT_DESC)
    private String shortDesc = "";

    @NotBlank
    @Size(min = Product.MIN_LENGTH_DETAIL_DESC, max = Product.MAX_LENGTH_DETAIL_DESC)
    private String detailDesc = "";

    @Positive
    private int categoryId;

    //@NotNull
    private List<ETarget> targets;
    public int getCategoryId() {
        return this.categoryId;
    }
    public List<ETarget> getTargets() {
        return this.targets;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId= categoryId;
    } 
    public void setTargets(List<ETarget> targets) {
        this.targets= targets;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(final String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(final String detailDesc) {
        this.detailDesc = detailDesc;
    }

}
