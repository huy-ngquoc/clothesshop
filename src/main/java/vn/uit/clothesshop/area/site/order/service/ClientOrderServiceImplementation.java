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
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;
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
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductWritePort productWritePort;
    private final ProductVariantWritePort productVariantWritePort;
    public ClientOrderServiceImplementation(UserReadPort userReadPort, CartPort cartPort, ProductVariantReadPort productVariantReadPort,
     OrderReadPort orderReadPort, OrderWritePort orderWritePort, OrderDetailWritePort orderDetailWritePort,
      OrderDetailReadPort orderDetailReadPort, ProductWritePort productWritePort,
      ProductVariantWritePort productVariantWritePort) {
        this.userReadPort = userReadPort;
        this.cartPort = cartPort;
        this.productVariantReadPort = productVariantReadPort;
        this.orderReadPort = orderReadPort;
        this.orderWritePort = orderWritePort;
        this.orderDetailWritePort= orderDetailWritePort;
        this.orderDetailReadPort = orderDetailReadPort;
        this.productWritePort = productWritePort;
        this.productVariantWritePort = productVariantWritePort;
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
       order=orderWritePort.save(order);
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
    @Transactional
    public Order createSingleOrder(long userId,SingleOrderRequest request) {
       User user = userReadPort.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
       Order order = new Order();
       order.setShippingFee(10000);
       
       order.setAddress(request.getAddress());
       order.setPhoneNumber(request.getPhoneNumber());
       order.setUser(user);
       order.setStatus(EOrderStatus.PROGRESSING);
       
       ProductVariant pv = productVariantReadPort.findById(request.getProductVariantId()).orElseThrow(()->new NotFoundException("Product variant not found"));
       if(pv.getStockQuantity()<request.getAmount()) {
            throw new OrderException("Not enough product");
       }
       order.setProductPrice(pv.getPriceCents()*request.getAmount());
       order=orderWritePort.save(order);
       OrderDetail orderDetail = new OrderDetail(order, pv, request.getAmount());
       orderDetailWritePort.save(orderDetail);
       return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(long orderId, long userId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(()->new NotFoundException("Order not found"));
        if(order.getUserId()!=userId) {
            throw new OrderException("You can not cancel this order");
        } 
        if(order.getStatus()!=EOrderStatus.PROGRESSING||order.getStatus()!=EOrderStatus.SHIPPING) {
            throw new OrderException("You can not cancel this order");
        }
        order.setStatus(EOrderStatus.CANCELED);
        if(order.getStatus()==EOrderStatus.SHIPPING) {
            List<OrderDetail> listDetails = orderDetailReadPort.findByOrderId(orderId);
            List<ProductVariant> listProductVariants=  new ArrayList<>();
            int totalAmount=0;
            Product p=null;
            for(OrderDetail detail: listDetails) {
                ProductVariant pv = detail.getProductVariant();
                if(p==null) {
                    p=pv.getProduct();
                }
                pv.setStockQuantity(pv.getStockQuantity()+detail.getAmount());
                totalAmount = totalAmount+detail.getAmount();
                listProductVariants.add(pv);
            }
            p.setQuantity(p.getQuantity()+totalAmount);
            productWritePort.save(p);
            productVariantWritePort.saveAll(listProductVariants);

        }
        return orderWritePort.save(order);

    }

    @Override
    
    public Order confirmReceiveOrder(long orderId, long userId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(()->new NotFoundException("Order not found"));
        if(order.getUserId()!=userId) {
            throw new OrderException("You can not cancel this order");
        } 
        if(order.getStatus()!=EOrderStatus.SHIPPING) {
            throw new OrderException("You can not cancel this order");
        }
        List<OrderDetail> listDetails = orderDetailReadPort.findByOrderId(orderId);
        List<ProductVariant> listProductVariants=  new ArrayList<>();
        int totalSold=0;
        Product p=null;
        for(OrderDetail detail: listDetails) {
                ProductVariant pv = detail.getProductVariant();
                if(p==null) {
                    p=pv.getProduct();
                }
                pv.setSold(pv.getSold()+detail.getAmount());
                totalSold = totalSold+detail.getAmount();
                listProductVariants.add(pv);
        }
        p.setSold(p.getSold()+totalSold);
        productWritePort.save(p);
        productVariantWritePort.saveAll(listProductVariants);
        order.setStatus(EOrderStatus.RECEIVED);
        return orderWritePort.save(order);


    }
    
}
