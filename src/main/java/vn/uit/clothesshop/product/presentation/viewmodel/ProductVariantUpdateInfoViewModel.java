package vn.uit.clothesshop.product.presentation.viewmodel;

import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;

public final class ProductVariantUpdateInfoViewModel {
    private final long productId;
    private final String imageFilePath;
    private final ProductVariantUpdateInfoForm form;

    public ProductVariantUpdateInfoViewModel(
            final long productId,
            final String imageFilePath,
            final ProductVariantUpdateInfoForm form) {
        this.productId = productId;
        this.imageFilePath = imageFilePath;
        this.form = form;
    }

    public long getProductId() {
        return productId;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public ProductVariantUpdateInfoForm getForm() {
        return form;
    }

}
