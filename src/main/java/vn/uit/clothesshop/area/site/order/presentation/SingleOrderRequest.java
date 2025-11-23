package vn.uit.clothesshop.area.site.order.presentation;

public class SingleOrderRequest extends OrderRequestInfo {
    private long productVariantId;
    private int amount;
    public SingleOrderRequest(String address, String phoneNumber,long productVariantId, int amount) {
        super(address, phoneNumber);
        this.productVariantId = productVariantId;
        this.amount = amount;
    } 
    public long getProductVariantId() {
        return this.productVariantId;
    } 
    public int getAmount() {
        return this.amount;
    }
}
