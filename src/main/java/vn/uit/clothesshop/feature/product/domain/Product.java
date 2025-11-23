package vn.uit.clothesshop.feature.product.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.feature.category.domain.Category;
import vn.uit.clothesshop.feature.product.domain.enums.ETarget;
import vn.uit.clothesshop.shared.util.EnumSetHelper;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
public class Product {
    public static final int MIN_LENGTH_NAME = 3;
    public static final int MAX_LENGTH_NAME = 200;
    public static final int MIN_LENGTH_SHORT_DESC = 3;
    public static final int MAX_LENGTH_SHORT_DESC = 500;
    public static final int MIN_LENGTH_DETAIL_DESC = 10;
    public static final int MAX_LENGTH_DETAIL_DESC = 10_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotBlank
    @Size(min = MIN_LENGTH_NAME, max = MAX_LENGTH_NAME)
    private String name = "";

    @NotBlank
    @Size(min = MIN_LENGTH_SHORT_DESC, max = MAX_LENGTH_SHORT_DESC)
    private String shortDesc = "";

    @NotBlank
    @Size(min = MIN_LENGTH_DETAIL_DESC, max = MAX_LENGTH_DETAIL_DESC)
    private String detailDesc = "";

    @Nullable
    private String image = null;

    @PositiveOrZero
    private int minPrice = 0;

    @PositiveOrZero
    private int maxPrice = 0;

    @PositiveOrZero
    private int quantity = 0;

    @PositiveOrZero
    private int sold = 0;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category = null;

    @ElementCollection(targetClass = ETarget.class)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<@NotNull ETarget> targets = EnumSet.noneOf(ETarget.class);

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = ProductVariant.Fields.product)
    @NotNull
    private List<@NotNull ProductVariant> variants = Collections.emptyList();

    public Product(
            final String name,
            final String shortDesc,
            final String detailDesc,
            final Category category,
            final Set<ETarget> target) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.category = category;
        this.targets = EnumSetHelper.copyOf(target, ETarget.class);
    }

    Product() {
    }

    public String getImage() {
        return this.image;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public int getSold() {
        return this.sold;
    }

    public void setSold(final int sold) {
        this.sold = sold;
    }

    public List<ProductVariant> getVariants() {
        return this.variants;
    }

    public long getCategoryId() {
        return this.category.getId();
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public Set<ETarget> getTargets() {
        return EnumSetHelper.copyOf(this.targets, ETarget.class);
    }

    public void setTargets(final Set<ETarget> target) {
        this.targets = EnumSetHelper.copyOf(target, ETarget.class);
    }

    public long getId() {
        return this.id;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(final int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(final int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(final String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDetailDesc() {
        return this.detailDesc;
    }

    public void setDetailDesc(final String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    void setId(final long id) {
        this.id = id;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
