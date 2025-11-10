package vn.uit.clothesshop.feature.category.domain;

import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
public class Category {
    public static final int MAX_LENGTH_NAME = 100;
    public static final int MAX_LENGTH_DESC = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotBlank
    @Size(max = MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(max = MAX_LENGTH_DESC)
    private String desc = "";

    @Nullable
    private String image = "";

    @PositiveOrZero
    private int amountOfProduct = 0;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Category(
            final String name,
            final String desc) {
        this.name = name;
        this.desc = desc;
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

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
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

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
