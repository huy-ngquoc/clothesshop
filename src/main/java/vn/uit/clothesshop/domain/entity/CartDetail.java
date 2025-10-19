package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import vn.uit.clothesshop.domain.embeddedkey.CartDetailKey;

@Entity
@Table(name="CartDetail")
public class CartDetail {
    @EmbeddedId
    private CartDetailKey id;
    @ManyToOne
    @MapsId("cartId")
    private Cart cart;
    @ManyToOne
    @MapsId("productVariantId")
    private ProductVariant productVariant;
    private int amount;
    public CartDetail() {

    }
    public CartDetail(CartDetailKey id, Cart cart, ProductVariant productVariant, int amount) {
        this.id = id;
        this.cart= cart;
        this.productVariant = productVariant;
        this.amount = amount;
    } 

    public CartDetailKey getId() {
        return this.id;
    } 
    public Cart getCart() {
        return this.cart;
    } 
    public ProductVariant getProductVariant() {
        return this.productVariant;
    } 
    public int getAmount() {
        return this.amount;
    } 
    public void setId(CartDetailKey id) {
        this.id = id;
    } 
    public void setCart(Cart cart) {
        this.cart = cart;
    } 
    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    } 
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
