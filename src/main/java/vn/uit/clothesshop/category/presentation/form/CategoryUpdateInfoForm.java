package vn.uit.clothesshop.category.presentation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.uit.clothesshop.category.domain.Category;

public final class CategoryUpdateInfoForm {
    @NotBlank
    @Size(max = Category.MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(max = Category.MAX_LENGTH_DESC)
    private String desc = "";

    public CategoryUpdateInfoForm(
            final String name,
            final String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }
}
