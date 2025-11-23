package vn.uit.clothesshop.feature.order.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user = new User();

    private String address;

    private String phoneNumber;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = OrderDetail.Fields.order)
    @NotNull
    private List<@NotNull OrderDetail> details = Collections.emptyList();

    private long productPrice = 0;

    private long shippingFee = 0;

    private long totalPrice = 0;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Order(
            final EOrderStatus status,
            final User user,
            final String address,
            String phoneNumber,
            final long productPrice,
            final long shippingFee,
            final long totalPrice) {
        this.status = status;
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    Order() {
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

    public long getUserId() {
        return user.getId();
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(final long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
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
