package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.domain.embeddedkey.CartDetailKey;

@Entity
@Table(name = "CartDetail")
public class CartDetail {
    @EmbeddedId
    private CartDetailKey id = new CartDetailKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailKey.Fields.cartId)
    private Cart cart = new Cart();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(CartDetailKey.Fields.productVariantId)
    private ProductVariant productVariant = new ProductVariant();

    @PositiveOrZero
    private int amount = 0;

    public CartDetail(
            final Cart cart,
            final ProductVariant productVariant,
            final int amount) {
        this.id = new CartDetailKey(cart.getId(), productVariant.getId());
        this.cart = cart;
        this.productVariant = productVariant;
        this.amount = amount;
    }

    CartDetail() {
    }

    public CartDetailKey getId() {
        return this.id;
    }

    public long getCartId() {
        return this.cart.getId();
    }

    public void setCart(final Cart cart) {
        this.cart = cart;
    }

    public long getProductVariantId() {
        return this.productVariant.getId();
    }

    public void setProductVariant(final ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
