package vn.uit.clothesshop.area.admin.order.mapper;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;

@Component
public class OrderAdminMapper {
    private final OrderDetailAdminMapper orderDetailAdminMapper;

    public OrderAdminMapper(OrderDetailAdminMapper orderDetailAdminMapper) {
        this.orderDetailAdminMapper = orderDetailAdminMapper;
    }

    @NonNull
    public OrderAdminBasicInfoViewModel toBasicInfo(
            @NonNull final Order order) {
        return new OrderAdminBasicInfoViewModel(
                order.getId(),
                order.getProductPrice(),
                order.getShippingFee(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    @NonNull
    public OrderAdminDetailInfoViewModel toDetailInfo(
            @NonNull final Order order,
            @NonNull final Page<OrderDetail> orderDetailsPage,
            @NonNull final Map<Long, ProductVariant> variantMap,
            @NonNull final Map<Long, Product> productMap) {
        final var detailViewModels = orderDetailsPage
                .map(detail -> {
                    final var variant = variantMap.get(detail.getProductVariantId());
                    if (variant == null) {
                        throw new NotFoundException("Product variant not found");
                    }

                    final var product = productMap.get(variant.getProductId());
                    if (product == null) {
                        throw new NotFoundException("Product not found");
                    }

                    return this.orderDetailAdminMapper.toViewModel(detail, variant, product);
                });

        return new OrderAdminDetailInfoViewModel(
                order.getStatus(),
                order.getProductPrice(),
                order.getShippingFee(),
                order.getAddress(),
                order.getPhoneNumber(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                detailViewModels);
    }
}
