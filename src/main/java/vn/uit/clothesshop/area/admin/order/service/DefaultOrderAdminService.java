package vn.uit.clothesshop.area.admin.order.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.clothesshop.area.admin.order.mapper.OrderAdminMapper;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.shared.service.StockAdjustmentService;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderDetailSpecification;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;

@Service
@RequiredArgsConstructor
public class DefaultOrderAdminService implements OrderAdminService {
    private final OrderReadPort orderReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final ProductVariantWritePort productVariantWritePort;
    private final ProductReadPort productReadPort;
    private final ProductWritePort productWritePort;
    private final OrderAdminMapper mapper;
    private final StockAdjustmentService stockAdjustmentService;

    @Override
    public Page<OrderAdminBasicInfoViewModel> findAllBasic(
            @Nullable Specification<Order> spec,
            @NonNull Pageable pageable) {
        return this.orderReadPort.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public OrderAdminDetailInfoViewModel findDetailById(
            long id,
            @NonNull Pageable pageable) {
        final var order = this.orderReadPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        final var orderDetailSpec = OrderDetailSpecification.orderIdEquals(id);
        final var orderDetailsPage = this.orderDetailReadPort.findAll(orderDetailSpec, pageable);
        final var variantIds = orderDetailsPage.stream()
                .map(OrderDetail::getProductVariantId)
                .collect(Collectors.toSet());

        final var variantMap = this.productVariantReadPort.findMapById(variantIds);
        final var productIds = variantMap.values().stream()
                .map(ProductVariant::getProductId)
                .collect(Collectors.toSet());

        final var productMap = this.productReadPort.findMapById(productIds);
        return this.mapper.toDetailInfo(order, orderDetailsPage, variantMap, productMap);
    }

    @Override
    @Transactional
    public void shipOrder(long orderId) {
        Order order = orderReadPort.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() != EOrderStatus.PROGRESSING) {
            throw new OrderException("You can not ship this order");
        }

        order.setStatus(EOrderStatus.SHIPPING);
        orderWritePort.save(order);

        stockAdjustmentService.adjustStockForOrder(orderId, (variant, product, amount) -> {
            int variantStock = variant.getStockQuantity();
            
            if (variantStock < amount) {
                throw new OrderException("Lack of variant stock");
            }

            int productStock = product.getQuantity();
            if (productStock < amount) {
                throw new OrderException("Lack of product stock");
            }

            variant.setStockQuantity(variantStock - amount);
            product.setQuantity(productStock - amount);
        });
    }

    @Override
    @Transactional
    public Order cancelOrder(long orderId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() != EOrderStatus.PROGRESSING) {
            throw new OrderException("You can not cancel this order");
        }
        order.setStatus(EOrderStatus.CANCELED);
        return orderWritePort.save(order);
    }

}
