package vn.uit.clothesshop.area.admin.order.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.uit.clothesshop.area.admin.order.service.AdminOrderService;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    private final UserService userService;
    public AdminOrderController(AdminOrderService adminOrderService, UserService userService) {
        this.adminOrderService = adminOrderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String getOrderPage(final Model model, @RequestParam(defaultValue="0") int pageNumber) {
        Page<Order> orders = adminOrderService.getOrder(pageNumber, 10);
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    @GetMapping("/order/{orderId}")
    public String getSingleOrder(final Model model, @PathVariable long orderId) {
        Order order = adminOrderService.getOrderById(orderId);
        model.addAttribute("order", order);
        List<OrderDetail> listDetails = adminOrderService.getOrderDetails(orderId);
        model.addAttribute("order_details",listDetails);
        return "admin/order/detail";
    } 

    @PostMapping("/order/cancel/{orderId}")
    public String cancelOrder(final Model model, @PathVariable long orderId) {
        
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        adminOrderService.cancelOrder(user.getId(), orderId);
        return "";
    } 

    @PostMapping("/order/ship/{orderId}") 
    public String shipOrder(final Model model, @PathVariable long orderId){
        User user = userService.getUserFromAuth();
        if(user==null) {
            return "redirect:/login";
        }
        adminOrderService.shipOrder(user.getId(), orderId);
        return "";
    }

}
