package vn.uit.clothesshop.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotBlank
    @Size(min = 3, max = 200)
    private String name = "";

    @NotBlank
    @Size(min = 3, max = 500)
    private String shortDesc = "";

    @NotBlank
    @Size(min = 10, max = 10000)
    private String detailDesc = "";

    @NotNull
    @PositiveOrZero
    @Digits(integer = 10, fraction = 2)
    @Column(precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    public long getId() {
        return this.id;
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

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    void setId(final long id) {
        this.id = id;
    }
}
