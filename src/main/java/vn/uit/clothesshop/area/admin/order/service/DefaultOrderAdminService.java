package vn.uit.clothesshop.area.admin.order.service;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.uit.clothesshop.area.admin.order.mapper.OrderAdminMapper;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderDetailSpecification;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;

@Service
public class DefaultOrderAdminService implements OrderAdminService {
    private final OrderReadPort orderReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final ProductVariantWritePort productVariantWritePort;
    private final ProductReadPort productReadPort;
    private final ProductWritePort productWritePort;
    private final OrderAdminMapper mapper;

    public DefaultOrderAdminService(
            OrderReadPort orderReadPort,
            OrderWritePort orderWritePort,
            OrderDetailReadPort orderDetailReadPort,
            ProductVariantReadPort productVariantReadPort,
            ProductVariantWritePort productVariantWritePort,
            ProductReadPort productReadPort,
            ProductWritePort productWritePort,
            ProductRepository productRepository,
            OrderAdminMapper mapper) {
        this.orderReadPort = orderReadPort;
        this.orderWritePort = orderWritePort;
        this.orderDetailReadPort = orderDetailReadPort;
        this.productVariantReadPort = productVariantReadPort;
        this.productVariantWritePort = productVariantWritePort;
        this.productReadPort = productReadPort;
        this.productWritePort = productWritePort;
        this.mapper = mapper;
    }

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

        var page = 0;
        var size = PagingConstraint.MAX_SIZE;

        final var orderDetailSpec = OrderDetailSpecification.orderIdEquals(orderId);
        var orderDetailPage = orderDetailReadPort.findAll(orderDetailSpec, PageRequest.of(page, size));
        final var totalOrderDetails = (int) Math.min(orderDetailPage.getTotalElements(), Integer.MAX_VALUE);

        final var variantMap = HashMap.<Long, ProductVariant>newHashMap(totalOrderDetails);
        final var productMap = HashMap.<Long, Product>newHashMap(totalOrderDetails);

        while (!orderDetailPage.isEmpty()) {
            for (final var orderDetail : orderDetailPage.getContent()) {
                final var productVariantId = orderDetail.getProductVariantId();
                var productVariant = variantMap.get(productVariantId);

                if (productVariant == null) {
                    productVariant = this.productVariantReadPort.findById(productVariantId)
                            .orElseThrow(() -> new NotFoundException("Product Variant not found"));
                    variantMap.put(productVariantId, productVariant);
                }

                final var productId = productVariant.getProductId();
                var product = productMap.get(productVariant.getProductId());

                if (product == null) {
                    product = this.productReadPort.findById(productId)
                            .orElseThrow(() -> new NotFoundException("Product not found"));
                    productMap.put(productId, product);
                }

                final var orderAmount = orderDetail.getAmount();

                final var variantStockQuantity = productVariant.getStockQuantity();
                if (variantStockQuantity < orderAmount) {
                    throw new OrderException("Lack of variant stock");
                }

                final var productStockQuantity = product.getQuantity();
                if (productStockQuantity < orderAmount) {
                    throw new OrderException("Lack of product stock");
                }

                productVariant.setStockQuantity(variantStockQuantity - orderAmount);
                product.setQuantity(productStockQuantity - orderAmount);

                this.productVariantWritePort.save(productVariant);
                this.productWritePort.save(product);
            }

            ++page;
            orderDetailPage = orderDetailReadPort.findAll(orderDetailSpec, PageRequest.of(page, size));
        }
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
