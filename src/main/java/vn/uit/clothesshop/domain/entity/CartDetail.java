package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.domain.embeddedkey.CartDetailKey;

@Entity
public class CartDetail {
    @EmbeddedId
    private CartDetailKey id = new CartDetailKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailKey.Fields.userId)
    private User user = new User();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailKey.Fields.productVariantId)
    private ProductVariant productVariant = new ProductVariant();

    @PositiveOrZero
    private int amount = 0;

    public CartDetail(
            final User user,
            final ProductVariant productVariant,
            final int amount) {
        this.id = new CartDetailKey(user.getId(), productVariant.getId());
        this.user = user;
        this.productVariant = productVariant;
        this.amount = amount;
    }

    CartDetail() {
    }

    public CartDetailKey getId() {
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

    void setId(final CartDetailKey id) {
        this.id = id;
    }

    void setUser(final User user) {
        this.id.setUserId(user.getId());
        this.user = user;
    }

}
