package vn.uit.clothesshop.feature.cart.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.feature.cart.domain.id.CartId;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.user.domain.User;

@Entity
public class Cart {
    @EmbeddedId
    private CartId id = new CartId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartId.Fields.userId)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartId.Fields.productVariantId)
    private ProductVariant productVariant;

    @PositiveOrZero
    private int amount = 0;

    public Cart(
            final User user,
            final ProductVariant productVariant,
            final int amount) {
        this.id = new CartId(user.getId(), productVariant.getId());
        this.user = user;
        this.productVariant = productVariant;
        this.amount = amount;
    }

    Cart() {
    }

    public CartId getId() {
        return this.id;
    }

    public long getUserId() {
        return this.user.getId();
    }

    public long getProductVariantId() {
        return this.productVariant.getId();
    }

    public void setProductVariant(final ProductVariant productVariant) {
        this.id.setProductVariantId(productVariant.getId());
        this.productVariant = productVariant;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    void setId(final CartId id) {
        this.id = id;
    }

    void setUser(final User user) {
        this.id.setUserId(user.getId());
        this.user = user;
    }

    public ProductVariant getProductVariant() {
        return this.productVariant;
    }

}
