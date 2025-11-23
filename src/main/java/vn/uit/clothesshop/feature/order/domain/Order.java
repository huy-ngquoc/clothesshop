package vn.uit.clothesshop.feature.order.domain;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.user.domain.User;

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

    private long shippingFee = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user = new User();

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    private String address;

    private String phoneNumber;

    public Order(
            final EOrderStatus status,
            final long shippingFee,
            final User user,
            final String address,
            String phoneNumber) {
        this.status = status;
        this.shippingFee = shippingFee;
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(final EOrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(final long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public long getUserId() {
        return user.getId();
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    void setId(final long id) {
        this.id = id;
    }

    void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
