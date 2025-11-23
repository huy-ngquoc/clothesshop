package vn.uit.clothesshop.area.site.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.cart.domain.port.CartPort;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;

@Service
class ClientOrderServiceImplementation implements ClientOrderService {
    private final UserReadPort userReadPort;
    private final CartPort cartPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderReadPort orderReadPort;
    private final OrderDetailWritePort orderDetailWritePort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductReadPort productReadPort;
    private final ProductWritePort productWritePort;
    private final ProductVariantWritePort productVariantWritePort;

    public ClientOrderServiceImplementation(
            UserReadPort userReadPort, CartPort cartPort,
            ProductVariantReadPort productVariantReadPort,
            OrderReadPort orderReadPort, OrderWritePort orderWritePort, OrderDetailWritePort orderDetailWritePort,
            OrderDetailReadPort orderDetailReadPort,
            ProductReadPort productReadPort,
            ProductWritePort productWritePort,
            ProductVariantWritePort productVariantWritePort) {
        this.userReadPort = userReadPort;
        this.cartPort = cartPort;
        this.productVariantReadPort = productVariantReadPort;
        this.orderReadPort = orderReadPort;
        this.orderWritePort = orderWritePort;
        this.orderDetailWritePort = orderDetailWritePort;
        this.orderDetailReadPort = orderDetailReadPort;
        this.productReadPort = productReadPort;
        this.productWritePort = productWritePort;
        this.productVariantWritePort = productVariantWritePort;
    }

    @Override
    @Transactional
    public Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo) {
        final var listCarts = cartPort.getCartsByUserId(userId);
        final var user = userReadPort.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        var order = new Order();
        order.setShippingFee(10000);
        order.setAddress(orderRequestInfo.getAddress());
        order.setPhoneNumber(orderRequestInfo.getPhoneNumber());
        order.setUser(user);
        order.setStatus(EOrderStatus.PROGRESSING);
        order = orderWritePort.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (final var cart : listCarts) {
            final var pv = cart.getProductVariant();
            if (cart.getAmount() > pv.getStockQuantity()) {
                throw new OrderException("Invalid quantity stock");
            }
            OrderDetail orderDetail = new OrderDetail(order, pv, cart.getAmount(), pv.getPriceCents());
            orderDetails.add(orderDetail);
        }

        orderDetailWritePort.saveAll(orderDetails);
        cartPort.deleteAll(listCarts);

        return order;

    }

    @Override
    @Transactional
    public Order createSingleOrder(long userId, SingleOrderRequest request) {
        final var user = userReadPort.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        var order = new Order();
        order.setShippingFee(10000);

        order.setAddress(request.getAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setUser(user);
        order.setStatus(EOrderStatus.PROGRESSING);

        ProductVariant pv = productVariantReadPort.findById(request.getProductVariantId())
                .orElseThrow(() -> new NotFoundException("Product variant not found"));
        if (pv.getStockQuantity() < request.getAmount()) {
            throw new OrderException("Not enough product");
        }
        order = orderWritePort.save(order);
        OrderDetail orderDetail = new OrderDetail(order, pv, request.getAmount(), pv.getPriceCents());
        orderDetailWritePort.save(orderDetail);
        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(long orderId, long userId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getUserId() != userId) {

            throw new OrderException("You can not cancel this order");
        }
        if ((order.getStatus() == EOrderStatus.PROGRESSING) || (order.getStatus() == EOrderStatus.SHIPPING)) {
            throw new OrderException("You can not cancel this order");
        }

        var page = 0;
        var size = PagingConstraint.MAX_SIZE;

        var orderDetailPage = orderDetailReadPort.findAllByOrderId(orderId, PageRequest.of(page, size));
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
                final var productStockQuantity = product.getQuantity();

                productVariant.setStockQuantity(variantStockQuantity + orderAmount);
                product.setQuantity(productStockQuantity + orderAmount);

                this.productVariantWritePort.save(productVariant);
                this.productWritePort.save(product);
            }

            ++page;
            orderDetailPage = orderDetailReadPort.findAllByOrderId(orderId, PageRequest.of(page, size));
        }

        order.setStatus(EOrderStatus.CANCELED);
        this.orderWritePort.save(order);
    }

    @Override
    @Transactional
    public void confirmReceiveOrder(long orderId, long userId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getUserId() != userId) {
            throw new OrderException("You can not cancel this order");
        }
        if (order.getStatus() != EOrderStatus.SHIPPING) {
            throw new OrderException("You can not cancel this order");
        }

        var page = 0;
        var size = PagingConstraint.MAX_SIZE;

        var orderDetailPage = orderDetailReadPort.findAllByOrderId(orderId, PageRequest.of(page, size));
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
                final var variantSold = productVariant.getSold();
                final var productSold = product.getSold();

                productVariant.setStockQuantity(variantSold + orderAmount);
                product.setQuantity(productSold + orderAmount);

                this.productVariantWritePort.save(productVariant);
                this.productWritePort.save(product);
            }
            ++page;
            orderDetailPage = orderDetailReadPort.findAllByOrderId(orderId, PageRequest.of(page, size));
        }

        order.setStatus(EOrderStatus.RECEIVED);
        orderWritePort.save(order);
    }

    @Override
    public Page<Order> getOrders(long userId, int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return orderReadPort.findAllByUserId(userId, pageable);
    }

    @Override
    public Order findOrderById(long orderId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(() -> new OrderException("Order not found"));
        return order;
    }

    @Override
    public Page<OrderDetail> findDetailsByOrderId(
            long orderId,
            @NonNull Pageable pageable) {
        return orderDetailReadPort.findAllByOrderId(orderId, pageable);
    }

}
