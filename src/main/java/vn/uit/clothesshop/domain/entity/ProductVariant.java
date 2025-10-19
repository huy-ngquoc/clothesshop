package vn.uit.clothesshop.domain.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
public class ProductVariant implements Comparable<ProductVariant>  {
    public static final int MAX_LENGTH_COLOR = 10;
    public static final int MAX_LENGTH_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product = new Product();

    @NotBlank
    @Size(max = MAX_LENGTH_COLOR)
    private String color = "";

    @NotBlank
    @Size(max = MAX_LENGTH_SIZE)
    private String size = "";

    @PositiveOrZero
    private int stockQuantity = 0;

    @PositiveOrZero
    private int priceCents = 0;

    @PositiveOrZero
    private int weightGrams = 0;

    @Nullable
    private String image = "";

    @PositiveOrZero
    @ColumnDefault("0")
    private int sold;

    ProductVariant() {
    }

    public ProductVariant(
            final Product product,
            final String color,
            final String size,
            final int stockQuantity,
            final int priceCents,
            final int weightGrams, final int sold) {
        this.product = product;
        this.color = color;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.priceCents = priceCents;
        this.weightGrams = weightGrams;
        this.sold= sold;
    }
    public int getSold() {
        return this.sold;
    } 
    public void setSold(int sold) {
        this.sold = sold;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return this.id;
    }

    public long getProductId() {
        return this.product.getId();
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(final int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getPriceCents() {
        return this.priceCents;
    }

    public void setPriceCents(final int priceCents) {
        this.priceCents = priceCents;
    }

    public int getWeightGrams() {
        return this.weightGrams;
    }

    public void setWeightGrams(final int weightGrams) {
        this.weightGrams = weightGrams;
    }

    @Override
    public int compareTo(ProductVariant o) {
        return Integer.compare(this.priceCents, o.priceCents);
    }
}