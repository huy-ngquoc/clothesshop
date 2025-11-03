package vn.uit.clothesshop.product.presentation.viewmodel;

import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;

public final class ProductVariantCreationInfoViewModel {
    private final ProductVariantCreationForm form;

    public ProductVariantCreationInfoViewModel(
            final ProductVariantCreationForm form) {
        this.form = form;
    }

    public ProductVariantCreationForm getForm() {
        return this.form;
    }

}
