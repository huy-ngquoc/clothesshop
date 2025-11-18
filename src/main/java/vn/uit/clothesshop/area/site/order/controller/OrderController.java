package vn.uit.clothesshop.area.site.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.area.site.order.service.ClientOrderService;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.infra.jpa.repository.CartRepository;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.service.UserService;


@Controller
@RequestMapping("/order")
public class OrderController {
    private final ClientOrderService clientOrderService;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductVariantReadPort productVariantReadPort;
    public OrderController(ClientOrderService clientOrderService, UserService userService, CartRepository cartRepository, ProductVariantReadPort productVariantReadPort) {
        this.clientOrderService = clientOrderService;
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.productVariantReadPort = productVariantReadPort;
    }

    @GetMapping("/cart")
    public String getOrderFromCartPage(final Model model) {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        List<Cart> carts = cartRepository.findByUser_Id(user.getId());
        model.addAttribute("cartItems", carts);
        OrderRequestInfo requestInfo = new OrderRequestInfo();
        model.addAttribute("request_info", requestInfo);
        return "client/order/show";
    }

    @PostMapping("/confirm/cart")
    public String confirmOrderFromCart(final Model model, @ModelAttribute("request_info") OrderRequestInfo requestInfo) {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        clientOrderService.createOrderFromCart(user.getId(), requestInfo);
        return "redirect:/";
    }

    @GetMapping("/single_product/{productVariantId}/{amount}")
    public String getSingleOrderPage(final Model model, @PathVariable long productVariantId, @PathVariable int amount) {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        
        ProductVariant pv = productVariantReadPort.findById(productVariantId).orElseThrow(()->new NotFoundException("Product variant not found"));
        List<Cart> listTempCarts = new ArrayList<>();
        listTempCarts.add(new Cart(user, pv, amount));
        model.addAttribute("cartItems", listTempCarts);
        SingleOrderRequest request = new SingleOrderRequest("", "", productVariantId, amount);
        model.addAttribute("request_info", request);
        return "client/order/show";
    }
    @PostMapping("/single_product_confirm")
    public String confirmSingleOrderPage(final Model model,@ModelAttribute("request_info") SingleOrderRequest request) {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        clientOrderService.createSingleOrder(user.getId(), request);
        return "";
    }
    @PostMapping("/cancel_order/{orderId}")
    public String cancelOrder(final Model model, @PathVariable long orderId) 
    {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        try {
            clientOrderService.cancelOrder(user.getId(), orderId);
        }
        catch(OrderException o) {
            return "";
        } 
        return "";
    }
    @PostMapping("/received_order/{orderId}")
    public String receivedOrder(final Model model, @PathVariable long orderId) {
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        try {
            clientOrderService.confirmReceiveOrder(user.getId(), orderId);
        } 
        catch(OrderException o) {
            return "";
        }
        return "";
    }

}
