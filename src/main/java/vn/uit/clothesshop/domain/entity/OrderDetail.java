package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import vn.uit.clothesshop.domain.embeddedkey.OrderDetailKey;

@Entity
@Table(name="OrderDetail")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailKey id;
    @ManyToOne
    @MapsId("orderId")
    private Order order;
    @ManyToOne
    @MapsId("productVariantId") 
    private ProductVariant productVariant;
    private int amount;
    public OrderDetail() {

    } 
    public OrderDetail(OrderDetailKey id, Order order, ProductVariant productVariant, int amount) {
        this.id = id;
        this.order = order;
        this.productVariant = productVariant;
        this.amount = amount;
    } 
    public OrderDetailKey getId() {
        return this.id;
    } 
    public Order getOrder() {
        return this.order;
    }
    public ProductVariant getProductVariant() {
        return this.productVariant;
    } 
    public int getAmount() {
        return this.amount;
    }

    public void setId(OrderDetailKey id) { 
        this.id = id;
    } 
    public void setOrder(Order order) {
        this.order = order;
    } 
    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    } 
    public void setAmount(int amount) {
        this.amount = amount;
    } 
}
