package vn.uit.clothesshop.product.presentation.viewmodel;

import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;

public final class ProductVariantUpdateImageViewModel {
    private final long productId;
    private final String imageFilePath;
    private final ProductVariantUpdateImageForm form;

    public ProductVariantUpdateImageViewModel(
            long productId,
            String imageFilePath,
            ProductVariantUpdateImageForm form) {
        this.productId = productId;
        this.imageFilePath = imageFilePath;
        this.form = form;
    }

    public long getProductId() {
        return this.productId;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

    public ProductVariantUpdateImageForm getForm() {
        return this.form;
    }

}
