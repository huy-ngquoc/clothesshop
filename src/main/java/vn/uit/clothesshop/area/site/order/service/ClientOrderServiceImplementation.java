package vn.uit.clothesshop.area.site.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.shared.service.StockAdjustmentService;
import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.domain.port.CartPort;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderDetailSpecification;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;

@Service
@RequiredArgsConstructor
class ClientOrderServiceImplementation implements ClientOrderService {
    private final UserReadPort userReadPort;
    private final CartPort cartPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderReadPort orderReadPort;
    private final OrderDetailWritePort orderDetailWritePort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final StockAdjustmentService stockAdjustmentService;

    @Override
    @Transactional
    public Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo) {
        final var listCarts = cartPort.getCartsByUserId(userId);

        long totalProductPrice = 0;
        for (final var cart : listCarts) {
            final var pv = cart.getProductVariant();
            if (cart.getAmount() > pv.getStockQuantity()) {
                throw new OrderException("Invalid quantity stock");
            }

            totalProductPrice += pv.getPriceCents() * cart.getAmount();
        }

        final var user = userReadPort.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        final var order = new Order(
                EOrderStatus.PROGRESSING,
                user,
                orderRequestInfo.getAddress(),
                orderRequestInfo.getPhoneNumber(),
                totalProductPrice,
                10_000,
                totalProductPrice + 10_000);
        final var savedOrder = this.orderWritePort.save(order);

        final var orderDetails = listCarts.stream()
                .map((Cart cart) -> {
                    var pv = cart.getProductVariant();
                    return new OrderDetail(
                            savedOrder,
                            pv,
                            cart.getAmount(),
                            pv.getPriceCents());
                })
                .toList();
        orderDetailWritePort.saveAll(orderDetails);
        cartPort.deleteAll(listCarts);

        return savedOrder;

    }

    @Override
    @Transactional
    public Order createSingleOrder(long userId, SingleOrderRequest request) {
        final var user = userReadPort.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        ProductVariant pv = productVariantReadPort.findById(request.getProductVariantId())
                .orElseThrow(() -> new NotFoundException("Product variant not found"));
        if (pv.getStockQuantity() < request.getAmount()) {
            throw new OrderException("Not enough product");
        }

        final var productPrice = pv.getPriceCents() * request.getAmount();
        final var order = new Order(
                EOrderStatus.PROGRESSING,
                user,
                request.getAddress(),
                request.getPhoneNumber(),
                productPrice,
                10_000,
                productPrice + 10_000);

        final var orderDetail = new OrderDetail(
                order,
                pv,
                request.getAmount(),
                pv.getPriceCents());

        final var savedOrder = orderWritePort.save(order);
        orderDetailWritePort.save(orderDetail);

        return savedOrder;
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

        this.stockAdjustmentService.adjustStockForOrder(orderId, (variant, product, amount) -> {
            if (order.getStatus() == EOrderStatus.SHIPPING) {
                variant.setStockQuantity(variant.getStockQuantity() + amount);
                product.setQuantity(product.getQuantity() + amount);
            }
        });

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

        this.stockAdjustmentService.adjustStockForOrder(orderId, (variant, product, amount) -> {
            variant.setSold(variant.getSold() + amount);
            product.setSold(product.getSold() + amount);
        });

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
        final var orderDetailSpec = OrderDetailSpecification.orderIdEquals(orderId);
        return orderDetailReadPort.findAll(orderDetailSpec, pageable);
    }

}
