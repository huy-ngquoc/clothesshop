package vn.uit.clothesshop.feature.cart.domain.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

@Embeddable
@FieldNameConstants
@EqualsAndHashCode
public class CartId implements Serializable {
    private long userId = 0;
    private long productVariantId = 0;

    public CartId() {
    }

    public CartId(
            final long userId,
            final long productVariantId) {
        this.userId = userId;
        this.productVariantId = productVariantId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public void setProductVariantId(final long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
