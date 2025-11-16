package vn.uit.clothesshop.area.site.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.domain.port.CartPort;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;
@Service
public class ClientOrderServiceImplementation implements ClientOrderService {
    private final UserReadPort userReadPort;
    private final CartPort cartPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderReadPort orderReadPort;
    private final OrderDetailWritePort orderDetailWritePort;
    public ClientOrderServiceImplementation(UserReadPort userReadPort, CartPort cartPort, ProductVariantReadPort productVariantReadPort,
     OrderReadPort orderReadPort, OrderWritePort orderWritePort, OrderDetailWritePort orderDetailWritePort) {
        this.userReadPort = userReadPort;
        this.cartPort = cartPort;
        this.productVariantReadPort = productVariantReadPort;
        this.orderReadPort = orderReadPort;
        this.orderWritePort = orderWritePort;
        this.orderDetailWritePort= orderDetailWritePort;
    }

    @Override
    @Transactional
    public Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo) {
       List<Cart> listCarts = cartPort.getCartsByUserId(userId);
       User user = userReadPort.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
       Order order = new Order();
       order.setShippingFee(10000);
       order.setProductPrice(0);
       order.setAddress(orderRequestInfo.getAddress());
       order.setPhoneNumber(orderRequestInfo.getPhoneNumber());
       order.setUser(user);
       order.setStatus(EOrderStatus.PROGRESSING);
       orderWritePort.save(order);
       List<OrderDetail> orderDetails= new ArrayList<>();
       for(Cart cart: listCarts) {
            ProductVariant pv = cart.getProductVariant();
            if(cart.getAmount()>pv.getStockQuantity()) {
                throw new OrderException("Invalid quantity stock");
            }
            OrderDetail orderDetail = new OrderDetail(order, pv, cart.getAmount());
            orderDetails.add(orderDetail);
            order.setProductPrice(order.getProductPrice()+pv.getPriceCents()*cart.getAmount());
       }
       orderDetailWritePort.saveAll(orderDetails);
       cartPort.deleteAll(listCarts);
       return order;
       
    }

    @Override
    public Order createSingleOrder(SingleOrderRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSingleOrder'");
    }

    @Override
    public Order cancelOrder(long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelOrder'");
    }

    @Override
    public Order confirmReceiveOrder(long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'confirmReceiveOrder'");
    }
    
}
