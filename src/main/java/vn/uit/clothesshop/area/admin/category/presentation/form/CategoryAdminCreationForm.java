package vn.uit.clothesshop.area.admin.category.presentation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.uit.clothesshop.feature.category.domain.Category;

public final class CategoryAdminCreationForm {
    @NotBlank
    @Size(max = Category.MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(max = Category.MAX_LENGTH_DESC)
    private String desc = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
