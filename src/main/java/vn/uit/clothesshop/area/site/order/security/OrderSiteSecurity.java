package vn.uit.clothesshop.area.site.order.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import vn.uit.clothesshop.config.security.UserPrincipal;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.service.UserService;

@Component
public class OrderSiteSecurity {
    private final OrderReadPort orderReadPort;
    private final UserService userService;
    public OrderSiteSecurity(
            final OrderReadPort orderReadPort, final UserService userService) {
        this.orderReadPort = orderReadPort;
        this.userService= userService;
    }

    public boolean isOwner(long orderId) {
final var auth = SecurityContextHolder.getContext().getAuthentication();
if (auth == null || !auth.isAuthenticated()){
    return false;
}

        User user = userService.getUserFromAuth(auth);

        return this.orderReadPort.existsByIdAndUserId(orderId, user.getId());
    }
}
