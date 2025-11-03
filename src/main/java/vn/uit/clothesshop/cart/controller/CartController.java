package vn.uit.clothesshop.cart.controller;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.uit.clothesshop.cart.domain.CartDetail;
import vn.uit.clothesshop.cart.dto.request.CartDetailRequest;
import vn.uit.clothesshop.cart.service.CartService;
import vn.uit.clothesshop.user.domain.User;
import vn.uit.clothesshop.user.repository.UserRepository;

@Controller
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepo;
    public CartController(CartService cartService, UserRepository userRepo) {
        this.cartService = cartService;
        this.userRepo = userRepo;
    } 
    @PostMapping("/add_to_cart")
    public String addProductVariantToCart(final Model model, @ModelAttribute("cart_request") CartDetailRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user ;
        Object principal = auth.getPrincipal();
        if(auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
        user = userRepo.findByEmail(u.getUsername()).orElse(null);
        CartDetail cartDetail= cartService.addProductVariantToCart(user, request.getProductVariantId(), request.getAmount());
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(final Model model) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user ;
        Object principal = auth.getPrincipal();
        if(auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
        user = userRepo.findByEmail(u.getUsername()).orElse(null);
        List<CartDetail> listCartDetails = cartService.getCartOfUser(user);
        model.addAttribute("listCartDetails",listCartDetails);
        return "client/cart/cart";
    }

    @PostMapping("/delete_cart_detail")
    public String deleteCart(@RequestParam("userId") long userId, @RequestParam("productVariantId") long productVariantId) {
        cartService.deleteCartDetail(userId, productVariantId);
        return "redirect:/client/cart/cart";
    }

    @PostMapping("/increase_cart_detail")
    public String increaseCartAmount(@RequestParam("userId") long userId, @RequestParam("productVariantId") long productVariantId) 
    {
        cartService.increaseCartAmount(userId, productVariantId);
        return "redirect:cart";
    }
    @PostMapping("/decrease_cart_detail")
    public String deleteCartAmount(@RequestParam("userId") long userId, @RequestParam("productVariantId") long productVariantId) {
        cartService.decreaseCartAmount(userId, productVariantId);
        return "redirect:cart";
    }
}
