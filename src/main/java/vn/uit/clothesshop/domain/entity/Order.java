package vn.uit.clothesshop.domain.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;

import vn.uit.clothesshop.domain.enums.EOrderStatus;

@Entity
@Table(name = "Orders")
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status = EOrderStatus.PROGRESSING;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private final Instant createAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private final Instant updateAt = Instant.now();

    private long productPrice = 0;

    private long shippingFee = 0;

    private long total = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user = new User();

    public Order(
            final EOrderStatus status,
            final long productPrice,
            final long shippingFee,
            final long total,
            final User user) {
        this.status = status;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.total = total;
        this.user = user;
    }

    Order() {
    }

    public long getId() {
        return id;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(final EOrderStatus status) {
        this.status = status;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(final long productPrice) {
        this.productPrice = productPrice;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(final long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public long getUserId() {
        return user.getId();
    }

    public void setUser(final User user) {
        this.user = user;
    }

    void setId(final long id) {
        this.id = id;
    }
}
