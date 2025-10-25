package vn.uit.clothesshop.domain.embeddedkey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.experimental.FieldNameConstants;

@Embeddable
@FieldNameConstants
public class CartDetailKey implements Serializable {
    private long cartId = 0;
    private long productVariantId = 0;

    public CartDetailKey() {
    }

    public CartDetailKey(
            final long cartId,
            final long productVariantId) {
        this.cartId = cartId;
        this.productVariantId = productVariantId;
    }

    public long getCartId() {
        return this.cartId;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public void setCartId(final long cartId) {
        this.cartId = cartId;
    }

    public void setProductVariantId(final long productVariantId) {
        this.productVariantId = productVariantId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CartDetailKey other) {
            return Objects.equals(this.cartId, other.cartId)
                    && Objects.equals(this.productVariantId, other.productVariantId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cartId, this.productVariantId);
    }
}
