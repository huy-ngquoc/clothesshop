package vn.uit.clothesshop.embeddedkey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartDetailKey implements Serializable {
    private Long cartId;
    private Long productVariantId;
    public CartDetailKey() {

    } 
    public CartDetailKey(Long cartId, Long productVariantId) {
        this.cartId = cartId;
        this.productVariantId = productVariantId;
    }
    public Long getCartId() {
        return this.cartId;
    } 
    public Long getProductVariantId() {
        return this.productVariantId;
    } 
    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }  
    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof CartDetailKey other) {
            return Objects.equals(this.cartId, other.cartId)&&Objects.equals(this.productVariantId, other.productVariantId);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.cartId, this.productVariantId);
    }
}
