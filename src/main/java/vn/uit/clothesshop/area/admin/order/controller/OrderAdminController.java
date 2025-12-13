package vn.uit.clothesshop.area.admin.order.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.uit.clothesshop.area.admin.order.service.OrderAdminService;
import vn.uit.clothesshop.area.shared.constant.ModelAttributeConstant;
import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;

@Controller
@RequestMapping("/admin/order")
public class OrderAdminController {
    private final OrderAdminService orderAdminService;

    public OrderAdminController(OrderAdminService adminOrderService) {
        this.orderAdminService = adminOrderService;
    }

    @GetMapping
    public String getOrderPage(
            final Model model,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var page = orderAdminService.findAllBasic(pageable);

        model.addAttribute(ModelAttributeConstant.PAGE, page);

        return "admin/order/show";
    }

    @GetMapping("/{orderId}")
    public String getSingleOrder(
            final Model model,
            @PathVariable long orderId,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var viewModel = orderAdminService.findDetailById(orderId, pageable);

        model.addAttribute(ModelAttributeConstant.ID, orderId);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/order/detail";
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable long orderId) {
        orderAdminService.cancelOrder(orderId);
        return "redirect:/admin/order/" + orderId;
    }

    @PostMapping("/ship/{orderId}")
    public String shipOrder(@PathVariable long orderId) {
        orderAdminService.shipOrder(orderId);
        return "redirect:/admin/order/" + orderId;
    }

}
