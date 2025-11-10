package vn.uit.clothesshop.area.admin.product.presentation.viewmodel;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminBasicInfoViewModel;

public final class ProductAdminInfoUpdateViewModel {
    private final Page<CategoryAdminBasicInfoViewModel> categoryPage;

    public ProductAdminInfoUpdateViewModel(Page<CategoryAdminBasicInfoViewModel> categoryPage) {
        this.categoryPage = categoryPage;
    }

    public Page<CategoryAdminBasicInfoViewModel> getCategoryPage() {
        return this.categoryPage;
    }
}
