package vn.uit.clothesshop.domain.entity;

import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import vn.uit.clothesshop.domain.enums.EPaymentStatus;
import vn.uit.clothesshop.domain.enums.EPaymentType;

@Entity
@Table(name="Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private long id;
    @OneToOne(cascade=CascadeType.ALL)
    private Order order;
    private String currency="VND";
    @Enumerated(EnumType.STRING)
    private EPaymentStatus status;
    @Enumerated(EnumType.STRING) 
    private EPaymentType paymentType;
    private long total;
    private Instant createAt;
    private Instant updateAt;
    public Payment() {

    } 
    public Payment(long id, Order order, String currency, EPaymentStatus status, EPaymentType paymentType, long total, Instant createAt, Instant updateAt) {
        this.id = id;
        this.order= order;
        this.currency= currency;
        this.status = status;
        this.paymentType = paymentType;
        this.total= total;
        this.createAt = createAt;
        this.updateAt= updateAt;
    }
    public long getId() {
        return this.id;
    } 
    public Order getOrder() {
        return this.order;
    } 
    public String getCurrency() {
        return this.currency;
    } 
    public EPaymentStatus getStatus() {
        return this.status;
    } 
    public EPaymentType getPaymentType() {
        return this.paymentType;
    }
    public long getTotal() {
        return this.total;
    } 
    public Instant getCreateAt() {
        return this.createAt;
    } 
    public Instant getUpdateAt() {
        return this.updateAt;
    }
    public void setId(long id) {
        this.id= id;
    } 
    public void setOrder(Order order) {
        this.order = order;
    } 
    public void setCurrency(String currency) {
        this.currency = currency;
    } 
    public void setStatus(EPaymentStatus status) {
        this.status = status;
    } 
    public void setPaymentType(EPaymentType paymentType) {
        this.paymentType = paymentType;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    } 
    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }
}
