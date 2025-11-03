package vn.uit.clothesshop.product.presentation.viewmodel;

import vn.uit.clothesshop.product.presentation.form.ProductUpdateInfoForm;

public final class ProductUpdateInfoViewModel {
    private final ProductUpdateInfoForm form;

    public ProductUpdateInfoViewModel(ProductUpdateInfoForm form) {
        this.form = form;
    }

    public ProductUpdateInfoForm getForm() {
        return this.form;
    }

}
