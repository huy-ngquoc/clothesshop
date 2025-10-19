package vn.uit.clothesshop.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Orders")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status;
    private Instant createAt;
    private Instant updateAt;
    private long productPrice;
    private long shippingFee;
    private long total;
    @ManyToOne
    @JoinColumn(name="UserId")
    private User user;
    public Order() {

    } 
    public Order(long id, EOrderStatus status, Instant createAt, Instant updateAt, long productPrice, long shippingFee, long total, User user) {
        this.id = id;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.total = total;
        this.user = user;
    }
    public long getId() {
        return this.id;
    } 
    public EOrderStatus getStatus() {
        return this.status;
    } 
    public Instant getCreateAt() {
        return this.createAt;
    } 
    public Instant getUpdateAt() {
        return this.updateAt;
    } 
    public long getProductPrice() {
        return this.productPrice;
    } 
    public long getShippingFee() {
        return this.shippingFee;
    } 
    public long getTotal() {
        return this.total;
    } 
    public User getUser() {
        return this.user;
    } 
    public void setId(long id) {
        this.id = id;
    } 
    public void setStatus(EOrderStatus status) {
        this.status = status;
    }
    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    } 
    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }
    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    } 
    public void setShippingFee(long shippingFee) {
        this.shippingFee = shippingFee;
    } 
    public void setTotal(long total) {
        this.total = total;
    }
}
