package vn.uit.clothesshop.area.site.order.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import vn.uit.clothesshop.config.security.UserPrincipal;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;

@Component
public class OrderSiteSecurity {
    private final OrderReadPort orderReadPort;

    public OrderSiteSecurity(
            final OrderReadPort orderReadPort) {
        this.orderReadPort = orderReadPort;
    }

    public boolean isOwner(long orderId, Authentication authentication) {
        final var userPrincipal = (UserPrincipal) authentication.getDetails();
        final var userId = userPrincipal.getId();

        return this.orderReadPort.existsByIdAndUserId(orderId, userId);
    }
}
