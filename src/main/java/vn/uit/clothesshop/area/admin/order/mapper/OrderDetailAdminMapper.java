package vn.uit.clothesshop.area.admin.order.mapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderDetailAdminViewModel;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;

@Component
public class OrderDetailAdminMapper {
    public OrderDetailAdminViewModel toViewModel(
            @NonNull final OrderDetail orderDetail,
            @NonNull final ProductVariant productVariant,
            @NonNull final Product product) {
        return new OrderDetailAdminViewModel(
                product.getId(),
                product.getName(),
                productVariant.getId(),
                productVariant.getColor(),
                productVariant.getSize());
    }
}
