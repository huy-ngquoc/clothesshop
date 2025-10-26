package vn.uit.clothesshop.domain.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.experimental.FieldNameConstants;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotBlank
    private String name = "";

    @Nullable
    private String image = "";

    @PositiveOrZero
    private int amountOfProduct = 0;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Category(
            final String name,
            final String image,
            final int amountOfProduct) {
        this.name = name;
        this.image = image;
        this.amountOfProduct = amountOfProduct;
    }

    Category() {
    }

    public long getId() {
        return id;
    }

    public int getAmountOfProduct() {
        return this.amountOfProduct;
    }

    public void setAmountOfProduct(final int amountOfProduct) {
        this.amountOfProduct = amountOfProduct;
    }

    void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
