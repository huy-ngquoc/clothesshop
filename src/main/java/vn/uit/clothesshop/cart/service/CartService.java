package vn.uit.clothesshop.cart.service;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.cart.domain.CartDetail;
import vn.uit.clothesshop.cart.domain.id.CartDetailId;
import vn.uit.clothesshop.cart.repository.CartDetailRepository;
import vn.uit.clothesshop.exception.NotFoundException;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.repository.ProductVariantRepository;
import vn.uit.clothesshop.user.domain.User;

@Service
public class CartService {
    private final CartDetailRepository cartDetailRepo;
    private final ProductVariantRepository productVariantRepo;
    public CartService(CartDetailRepository cartDetailRepo, ProductVariantRepository productVariantRepo) {
        this.cartDetailRepo = cartDetailRepo;
        this.productVariantRepo = productVariantRepo;
    } 
    
    public CartDetail addProductVariantToCart(User user, long productVariantId, int amount) throws NotFoundException {
        
        CartDetail oldCartDetail = cartDetailRepo.findById(new CartDetailId(user.getId(), productVariantId)).orElse(null);
        CartDetail cartDetail;
        if(oldCartDetail==null) {
            ProductVariant pv = productVariantRepo.findById(productVariantId).orElseThrow(()-> new NotFoundException("Variant not found"));
            cartDetail = new CartDetail(user, pv,amount);
        }
        else {
            cartDetail=oldCartDetail;
            cartDetail.setAmount(amount);
        }
        cartDetail = cartDetailRepo.save(cartDetail);
        return cartDetail;

    }
}
