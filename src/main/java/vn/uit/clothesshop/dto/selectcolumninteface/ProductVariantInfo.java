package vn.uit.clothesshop.dto.selectcolumninteface;

public interface ProductVariantInfo {
    public long getId();
    public String getColor();
    public String getSize();
    public int getStockQuantity();
    public int getPriceCents();
    public int getWeightGrams();
    public String getImage();
}
