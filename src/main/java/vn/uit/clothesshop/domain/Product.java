package vn.uit.clothesshop.domain;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@Entity
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

    @PositiveOrZero
    @ColumnDefault("0")
    private int minPrice;
    @PositiveOrZero
    @ColumnDefault("0")
    private int maxPrice;

    @ManyToOne
    private Category category;
    @ElementCollection(targetClass=ETarget.class)
    @Enumerated(EnumType.STRING)
    private List<ETarget> target;
    
    Product() {
    }

    public Product(
            final String name,
            final String shortDesc,
            final String detailDesc, final int minPrice, final int maxPrice,Category category, List<ETarget> target) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.minPrice= minPrice;
        this.maxPrice= maxPrice;
        this.category = category;
        this.target = target;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ETarget> getTarget() {
        return target;
    }

    public void setTarget(List<ETarget> target) {
        this.target = target;
    }

    public long getId() {
        return this.id;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
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

    void setId(final long id) {
        this.id = id;
    }
}
