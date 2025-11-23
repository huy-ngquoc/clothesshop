package vn.uit.clothesshop.area.admin.order.presentation.viewmodel;

public final class OrderDetailAdminViewModel {
    private final long productId;
    private final String productName;
    private final long variantId;
    private final String color;
    private final String size;
    private final int unitPrice;
    private final int amount;

    public OrderDetailAdminViewModel(
            final long productId,
            final String productName,
            final long variantId,
            final String color,
            final String size,
            final int unitPrice,
            final int amount) {
        this.productId = productId;
        this.productName = productName;
        this.variantId = variantId;
        this.color = color;
        this.size = size;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public long getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public long getVariantId() {
        return this.variantId;
    }

    public String getColor() {
        return this.color;
    }

    public String getSize() {
        return this.size;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getAmount() {
        return amount;
    }

}
