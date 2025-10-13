package vn.uit.clothesshop.dto.request;

public class ProductVariantUpdateImageRequest {
    private long productVariantId;
    private byte[] imageData;
    public long getProductVariantId() {
        return productVariantId;
    }
    public void setProductVariantId(long productVariantId) {
        this.productVariantId = productVariantId;
    }
    public byte[] getImageData() {
        if(imageData==null) return null;
        return imageData.clone();
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public ProductVariantUpdateImageRequest() {
    }
    public ProductVariantUpdateImageRequest(long productVariantId, byte[] imageData) {
        this.productVariantId = productVariantId;
        this.imageData = imageData;
    }
    
}
