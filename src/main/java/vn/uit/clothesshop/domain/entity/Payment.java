package vn.uit.clothesshop.domain.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.domain.enums.EPaymentStatus;
import vn.uit.clothesshop.domain.enums.EPaymentType;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private Order order = new Order();

    private String currency = "";

    @Enumerated(EnumType.STRING)
    private EPaymentStatus status = EPaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    private EPaymentType paymentType = EPaymentType.ONLINE;

    private long total = 0;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updateAt = Instant.now();

    public Payment(
            final long id,
            final Order order,
            final String currency,
            final EPaymentStatus status,
            final EPaymentType paymentType,
            final long total) {
        this.id = id;
        this.order = order;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.total = total;
    }

    Payment() {
    }

    public long getId() {
        return this.id;
    }

    public long getOrderId() {
        return this.order.getId();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public EPaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(EPaymentStatus status) {
        this.status = status;
    }

    public EPaymentType getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(EPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    void setId(long id) {
        this.id = id;
    }
}
