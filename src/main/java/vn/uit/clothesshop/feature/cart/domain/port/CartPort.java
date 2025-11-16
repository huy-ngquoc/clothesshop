package vn.uit.clothesshop.feature.cart.domain.port;

import java.util.List;

import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.user.domain.User;

public interface CartPort {
    Cart addProductVariantToCart(User user, long productVariantId, int amount);
    List<Cart> getCartsByUserId(long userId);
    void deleteAll(List<Cart> carts);
}
