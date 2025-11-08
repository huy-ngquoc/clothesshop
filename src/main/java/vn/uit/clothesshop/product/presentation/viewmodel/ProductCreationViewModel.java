package vn.uit.clothesshop.product.presentation.viewmodel;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;

public final class ProductCreationViewModel {
    private final Page<CategoryBasicInfoViewModel> categoryPage;

    public ProductCreationViewModel(Page<CategoryBasicInfoViewModel> categoryPage) {
        this.categoryPage = categoryPage;
    }

    public Page<CategoryBasicInfoViewModel> getCategoryPage() {
        return this.categoryPage;
    }

}
