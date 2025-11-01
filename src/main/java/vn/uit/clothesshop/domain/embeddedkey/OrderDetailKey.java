package vn.uit.clothesshop.domain.embeddedkey;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.experimental.FieldNameConstants;

@Embeddable
@FieldNameConstants
public class OrderDetailKey implements Serializable {
    private long orderId = 0;
    private long productVariantId = 0;

    public OrderDetailKey() {
    }

    public OrderDetailKey(
            final long orderId,
            final long productVariantId) {
        this.orderId = orderId;
        this.productVariantId = productVariantId;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public void setOrderId(final long orderId) {
        this.orderId = orderId;
    }

    public void setProductVariantId(final long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
