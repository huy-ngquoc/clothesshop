package vn.uit.clothesshop.cart.dto.request;

public class CartDetailRequest {
    private long productVariantId;
    private int amount;
    public CartDetailRequest(long productVariantId, int amount) {
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
    public CartDetailRequest() {
        
    }
}
