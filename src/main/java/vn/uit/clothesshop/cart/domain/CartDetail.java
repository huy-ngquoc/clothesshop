package vn.uit.clothesshop.cart.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.cart.domain.id.CartDetailId;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.user.domain.User;

@Entity
public class CartDetail {
    @EmbeddedId
    private CartDetailId id = new CartDetailId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailId.Fields.userId)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailId.Fields.productVariantId)
    private ProductVariant productVariant;

    @PositiveOrZero
    private int amount = 0;

    public CartDetail(
            final User user,
            final ProductVariant productVariant,
            final int amount) {
        this.id = new CartDetailId(user.getId(), productVariant.getId());
        this.user = user;
        this.productVariant = productVariant;
        this.amount = amount;
    }

    CartDetail() {
    }

    public CartDetailId getId() {
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

    void setId(final CartDetailId id) {
        this.id = id;
    }

    void setUser(final User user) {
        this.id.setUserId(user.getId());
        this.user = user;
    }

}
