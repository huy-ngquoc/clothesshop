package vn.uit.clothesshop.feature.order.infra.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.repository.OrderDetailRepository;

@Repository
public class OrderDetailAdapter implements OrderDetailReadPort, OrderDetailWritePort {
    private final OrderDetailRepository orderDetailRepo;
    public OrderDetailAdapter(OrderDetailRepository orderDetailRepo){
        this.orderDetailRepo = orderDetailRepo;
    }
    @Override
    public OrderDetail save(OrderDetail orderDetail) {
       return orderDetailRepo.save(orderDetail);
    }
    @Override
    public void deleteById(OrderDetailId id) {
        orderDetailRepo.deleteById(id);
    }
    @Override
    public void delete(OrderDetail orderDetail) {
        orderDetailRepo.delete(orderDetail);
    }
    @Override
    public void saveAll(List<OrderDetail> orderDetails) {
        orderDetailRepo.saveAll(orderDetails);
    }
    @Override
    public Page<OrderDetail> findAll(Specification<OrderDetail> spec, Pageable pageable) {
        return orderDetailRepo.findAll(spec, pageable);
    }
    @Override
    public Optional<OrderDetail> findById(OrderDetailId id) {
        return orderDetailRepo.findById(id);
    }
    @Override
    public boolean existsById(OrderDetailId id) {
       return orderDetailRepo.existsById(id);
    } 
    
    
}
