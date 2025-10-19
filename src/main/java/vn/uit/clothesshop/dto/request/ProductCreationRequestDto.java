package vn.uit.clothesshop.dto.request;

import java.util.EnumSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.uit.clothesshop.domain.ETarget;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.utils.EnumSetHelper;

public final class ProductCreationRequestDto {
    @NotBlank
    @Size(min = Product.MIN_LENGTH_NAME, max = Product.MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(min = Product.MIN_LENGTH_SHORT_DESC, max = Product.MAX_LENGTH_SHORT_DESC)
    private String shortDesc = "";

    @NotBlank
    @Size(min = Product.MIN_LENGTH_DETAIL_DESC, max = Product.MAX_LENGTH_DETAIL_DESC)
    private String detailDesc = "";

    private int categoryId = 0;

    @NotNull
    private EnumSet<ETarget> targets = EnumSet.noneOf(ETarget.class);

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final int categoryId) {
        this.categoryId = categoryId;
    }

    public Set<ETarget> getTargets() {
        return EnumSet.copyOf(this.targets);
    }

    public void setTargets(final Set<ETarget> targets) {
        this.targets = EnumSetHelper.copyOf(targets, ETarget.class);
    }

}
