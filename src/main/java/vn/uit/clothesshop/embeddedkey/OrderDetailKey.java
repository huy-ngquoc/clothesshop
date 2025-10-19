package vn.uit.clothesshop.embeddedkey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderDetailKey implements Serializable {
    private Long orderId;
    private Long productVariantId;
    public OrderDetailKey(){

    } 
    public OrderDetailKey(Long orderId, Long productVariantId) {
        this.orderId = orderId;
        this.productVariantId = productVariantId;
    } 
    public Long getOrderId() {
        return this.orderId;
    } 
    public Long getProductVariantId() {
        return this.productVariantId;
    } 
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    } 
    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof OrderDetailKey other) {
            return Objects.equals(this.orderId, other.orderId)&&Objects.equals(this.productVariantId, other.productVariantId);
        } 
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.orderId, this.productVariantId);
    }
}
