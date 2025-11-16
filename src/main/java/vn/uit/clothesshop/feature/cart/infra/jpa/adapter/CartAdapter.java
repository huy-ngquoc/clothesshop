package vn.uit.clothesshop.feature.cart.infra.jpa.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.domain.id.CartId;
import vn.uit.clothesshop.feature.cart.domain.port.CartPort;
import vn.uit.clothesshop.feature.cart.infra.jpa.repository.CartRepository;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductVariantRepository;
import vn.uit.clothesshop.feature.user.domain.User;

@Service
public class CartAdapter implements CartPort {
    private final CartRepository cartDetailRepo;
    private final ProductVariantRepository productVariantRepo;

    public CartAdapter(CartRepository cartDetailRepo, ProductVariantRepository productVariantRepo) {
        this.cartDetailRepo = cartDetailRepo;
        this.productVariantRepo = productVariantRepo;
    }

    public Cart addProductVariantToCart(User user, long productVariantId, int amount) throws NotFoundException {

        Cart oldCartDetail = cartDetailRepo.findById(new CartId(user.getId(), productVariantId))
                .orElse(null);
        Cart cartDetail;
        if (oldCartDetail == null) {
            ProductVariant pv = productVariantRepo.findById(productVariantId)
                    .orElseThrow(() -> new NotFoundException("Variant not found"));
            cartDetail = new Cart(user, pv, amount);
            cartDetail = cartDetailRepo.save(cartDetail);
            return cartDetail;
        } else {
            oldCartDetail.setAmount(amount);
            return cartDetailRepo.save(oldCartDetail);
        }

    }

    public List<Cart> getCartOfUser(User user) {
        return cartDetailRepo.findByUser_Id(user.getId());
    }

    public void deleteCartDetail(long userId, long productVariantId) {
        CartId cartDetailId = new CartId(userId, productVariantId);
        cartDetailRepo.deleteById(cartDetailId);
    }

    public void increaseCartAmount(long userId, long productVariantId) {
        CartId cartDetailId = new CartId(userId, productVariantId);
        Cart cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if (cartDetail != null) {
            cartDetail.setAmount(cartDetail.getAmount() + 1);
            cartDetailRepo.save(cartDetail);
        }
    }

    public void decreaseCartAmount(long userId, long productVariantId) {
        CartId cartDetailId = new CartId(userId, productVariantId);
        Cart cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if (cartDetail != null && cartDetail.getAmount() > 1) {
            cartDetail.setAmount(cartDetail.getAmount() - 1);
            cartDetailRepo.save(cartDetail);
        }
    }

    public void clearCart(long userId) {
        List<Cart> listCartDetails = cartDetailRepo.findByUser_Id(userId);
        cartDetailRepo.deleteAll(listCartDetails);
    }

    public void updateAmount(long userId, long productVariantId, int amount) {
        CartId cartDetailId = new CartId(userId, productVariantId);
        Cart cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if (cartDetail != null && amount > 0) {
            cartDetail.setAmount(amount);
            cartDetailRepo.save(cartDetail);
        }
    }

    @Override
    public List<Cart> getCartsByUserId(long userId) {
        return cartDetailRepo.findByUser_Id(userId);
    }

    @Override
    public void deleteAll(List<Cart> carts) {
        cartDetailRepo.deleteAll(carts);
    }
}
