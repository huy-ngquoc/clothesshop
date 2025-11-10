package vn.uit.clothesshop.area.site.cart.presentation.request;

public class CartRequest {
    private long productVariantId;
    private int amount;

    public CartRequest(long productVariantId, int amount) {
        this.productVariantId = productVariantId;
        this.amount = amount;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setProductVariantId(long productVariantId) {
        this.productVariantId = productVariantId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CartRequest() {

    }
}
