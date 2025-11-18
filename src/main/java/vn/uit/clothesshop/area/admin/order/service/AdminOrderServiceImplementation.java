package vn.uit.clothesshop.area.admin.order.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.area.shared.exception.advice.NoPermissionException;
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
public class AdminOrderServiceImplementation implements AdminOrderService {
    private final UserReadPort userReadPort;
    private final OrderReadPort orderReadPort;
    private final OrderWritePort orderWritePort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductVariantWritePort productVariantWritePort;
    private final ProductWritePort productWritePort;
    
    public AdminOrderServiceImplementation(UserReadPort userReadPort, OrderReadPort orderReadPort, OrderWritePort orderWritePort,
    OrderDetailReadPort orderDetailReadPort,ProductVariantWritePort productVariantWritePort, ProductWritePort productWritePort
    ) {
        this.userReadPort = userReadPort;
        this.orderReadPort = orderReadPort;
        this.orderWritePort = orderWritePort;
        this.orderDetailReadPort = orderDetailReadPort;
        
        this.productVariantWritePort = productVariantWritePort;
        this.productWritePort = productWritePort;
    }
    @Override
    @Transactional
    public Order shipOrder(long userId, long orderId) {
       Order order = orderReadPort.findById(orderId).orElseThrow(()->new NotFoundException("Order not found"));
       User user = userReadPort.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        if(user.getRole()!=User.Role.ADMIN) {
            throw new NoPermissionException("You can not ship this order");
        }
        if(order.getStatus()!=EOrderStatus.PROGRESSING) {
            throw new OrderException("You can not ship this order");
        }
        order.setStatus(EOrderStatus.SHIPPING);
        List<OrderDetail> listDetails = orderDetailReadPort.findByOrderId(orderId);
        List<ProductVariant> listProductVariants=  new ArrayList<>();
        int totalAmount=0;
        Product p=null;
        for(OrderDetail detail: listDetails) {
            ProductVariant pv = detail.getProductVariant();
            if(p==null) {
                p=pv.getProduct();
            }
            pv.setStockQuantity(pv.getStockQuantity()-detail.getAmount());
            totalAmount = totalAmount+detail.getAmount();
            listProductVariants.add(pv);
        }
        p.setQuantity(p.getQuantity()-totalAmount);
        productWritePort.save(p);
        productVariantWritePort.saveAll(listProductVariants);
        return orderWritePort.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(long userId, long orderId) {
        Order order = orderReadPort.findById(orderId).orElseThrow(()->new NotFoundException("Order not found"));
        User user = userReadPort.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        if(user.getRole()!=User.Role.ADMIN) {
            throw new NoPermissionException("You can not cancel this order");
        }
        if(order.getStatus()!=EOrderStatus.PROGRESSING) {
            throw new OrderException("You can not cancel this order");
        }
        order.setStatus(EOrderStatus.CANCELED);
        return orderWritePort.save(order);
    }
    @Override
    public Page<Order> getOrder(int pageNumber, int size) {
       Pageable pageable =PageRequest.of(pageNumber, size);
       return orderReadPort.findAll(null, pageable);


    }
    @Override
    public Order getOrderById(long orderId) {
       return orderReadPort.findById(orderId).orElseThrow(()->new OrderException("Order not found"));
    }
    @Override
    public List<OrderDetail> getOrderDetails(long orderId) {
        return orderDetailReadPort.findByOrderId(orderId);
    }
    
}
