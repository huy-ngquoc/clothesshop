package vn.uit.clothesshop.cart.service;

import java.util.List;

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
            cartDetail = cartDetailRepo.save(cartDetail);
            return cartDetail;
        }
        else {
            oldCartDetail.setAmount(amount);
            return cartDetailRepo.save(oldCartDetail);
        }
        
        

    }

    public List<CartDetail> getCartOfUser(User user) {
        return cartDetailRepo.findByUser_Id(user.getId());
    }

    public void deleteCartDetail(long userId, long productVariantId) {
        CartDetailId cartDetailId = new CartDetailId(userId, productVariantId);
        cartDetailRepo.deleteById(cartDetailId);
    }

    public void increaseCartAmount(long userId, long productVariantId) {
        CartDetailId cartDetailId = new CartDetailId(userId, productVariantId);
        CartDetail cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if(cartDetail!=null) {
            cartDetail.setAmount(cartDetail.getAmount()+1);
            cartDetailRepo.save(cartDetail);
        }
    }
    public void decreaseCartAmount(long userId, long productVariantId) {
        CartDetailId cartDetailId = new CartDetailId(userId, productVariantId);
        CartDetail cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if(cartDetail!=null&&cartDetail.getAmount()>1) {
            cartDetail.setAmount(cartDetail.getAmount()-1);
            cartDetailRepo.save(cartDetail);
        }
    }
    public void clearCart(long userId) {
        List<CartDetail> listCartDetails = cartDetailRepo.findByUser_Id(userId);
        cartDetailRepo.deleteAll(listCartDetails);
    }

    public void updateAmount(long userId, long productVariantId, int amount) {
         CartDetailId cartDetailId = new CartDetailId(userId, productVariantId);
        CartDetail cartDetail = cartDetailRepo.findById(cartDetailId).orElse(null);
        if(cartDetail!=null&&amount>0) {
            cartDetail.setAmount(amount);
            cartDetailRepo.save(cartDetail);
        }
    }
}
