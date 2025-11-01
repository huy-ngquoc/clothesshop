package vn.uit.clothesshop.domain.embeddedkey;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.experimental.FieldNameConstants;

@Embeddable
@FieldNameConstants
public class CartDetailKey implements Serializable {
    private long userId = 0;
    private long productVariantId = 0;

    public CartDetailKey() {
    }

    public CartDetailKey(
            final long userId,
            final long productVariantId) {
        this.userId = userId;
        this.productVariantId = productVariantId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public void setProductVariantId(final long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
