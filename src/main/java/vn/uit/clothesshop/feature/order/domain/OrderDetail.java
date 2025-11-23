package vn.uit.clothesshop.feature.order.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;

@Entity
@FieldNameConstants
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id = new OrderDetailId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(OrderDetailId.Fields.orderId)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(OrderDetailId.Fields.productVariantId)
    private ProductVariant productVariant;

    private int unitPrice = 0;

    private int amount = 0;

    public OrderDetail(
            final Order order,
            final ProductVariant productVariant,
            final int unitPrice,
            final int amount) {
        this.id = new OrderDetailId(order.getId(), productVariant.getId());
        this.order = order;
        this.productVariant = productVariant;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    OrderDetail() {
    }

    public ProductVariant getProductVariant() {
        return this.productVariant;
    }

    public OrderDetailId getId() {
        return this.id;
    }

    public long getOrderId() {
        return this.order.getId();
    }

    public long getProductVariantId() {
        return this.productVariant.getId();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setOrder(final Order order) {
        this.id.setOrderId(order.getId());
        this.order = order;
    }

    public void setProductVariant(final ProductVariant productVariant) {
        this.id.setProductVariantId(productVariant.getId());
        this.productVariant = productVariant;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Order getOrder() {
        return this.order;
    }
}
