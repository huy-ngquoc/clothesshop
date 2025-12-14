package vn.uit.clothesshop.area.site.cart.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.uit.clothesshop.area.site.cart.presentation.request.CartRequest;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.infra.jpa.adapter.CartAdapter;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.infra.jpa.repository.UserRepository;

@Controller

public class CartController {
    private final CartAdapter cartService;
    private final UserRepository userRepo;

    public CartController(CartAdapter cartService, UserRepository userRepo) {
        this.cartService = cartService;
        this.userRepo = userRepo;
    }

    @PostMapping("/add_to_cart")
    public String addProductVariantToCart(final Model model,
            @ModelAttribute("cart_request") CartRequest request, Authentication auth) {
        if(auth==null) {
            return "redirect:/auth/login";
        }
        String username = auth.getName();
        User user = userRepo.findByUsername(username).orElse(null);
        Cart cartDetail = cartService.addProductVariantToCart(user, request.getProductVariantId(),
                request.getAmount());
        return "redirect:cart";
    }

    @GetMapping("/cart")
    public String getCartPage(final Model model, Authentication auth) {

        if(auth==null) {
            return "redirect:auth/login";
        }
        String username = auth.getName();
        User user = userRepo.findByUsername(username).orElse(null);
        List<Cart> listCartDetails = cartService.getCartOfUser(user);
        model.addAttribute("listCartDetails", listCartDetails);
        model.addAttribute("user", user);
        return "client/cart/cart";
    }

    @PostMapping("/delete_cart_detail")
    public String deleteCart(@RequestParam("userId") long userId,
            @RequestParam("productVariantId") long productVariantId) {
        cartService.deleteCartDetail(userId, productVariantId);
        return "redirect:cart";
    }

    @PostMapping("/increase_cart_detail")
    public String increaseCartAmount(@RequestParam("userId") long userId,
            @RequestParam("productVariantId") long productVariantId) {
        cartService.increaseCartAmount(userId, productVariantId);
        return "redirect:cart";
    }

    @PostMapping("/decrease_cart_detail")
    public String deleteCartAmount(@RequestParam("userId") long userId,
            @RequestParam("productVariantId") long productVariantId) {
        cartService.decreaseCartAmount(userId, productVariantId);
        return "redirect:cart";
    }

    @PostMapping("/clear_cart")
    public String clearCart(@RequestParam("userId") long userId) {
        cartService.clearCart(userId);
        return "redirect:shop";
    }

    @PostMapping("/update_cart")
    public String updateCart(@RequestParam("userId") long userId,
            @RequestParam("productVariantId") long productVariantId, @RequestParam("amount") int amount) {
        cartService.updateAmount(userId, productVariantId, amount);
        return "redirect:cart";

    }
}
