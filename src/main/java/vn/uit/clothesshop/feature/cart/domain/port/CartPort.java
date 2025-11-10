package vn.uit.clothesshop.feature.cart.domain.port;

import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.user.domain.User;

public interface CartPort {
    Cart addProductVariantToCart(User user, long productVariantId, int amount);
}
