package vn.uit.clothesshop.feature.order.domain.id;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

@Embeddable
@FieldNameConstants
@EqualsAndHashCode
public class OrderDetailId implements Serializable {
    private long orderId = 0;
    private long productVariantId = 0;

    public OrderDetailId() {
    }

    public OrderDetailId(
            final long orderId,
            final long productVariantId) {
        this.orderId = orderId;
        this.productVariantId = productVariantId;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(final long orderId) {
        this.orderId = orderId;
    }

    public long getProductVariantId() {
        return this.productVariantId;
    }

    public void setProductVariantId(final long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
