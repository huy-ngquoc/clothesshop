package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;
import vn.uit.clothesshop.area.admin.statistic.model.StatisticModel;
import vn.uit.clothesshop.feature.category.domain.port.CategoryReadPort;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.util.TimeConverter;
@Service
public class StatisticServiceImplementation implements StatisticService {
    private final OrderReadPort orderReadPort;
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductReadPort productReadPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final CategoryReadPort categoryReadPort;
    public StatisticServiceImplementation(OrderReadPort orderReadPort, OrderDetailReadPort orderDetailReadPort, ProductReadPort productReadPort,
     ProductVariantReadPort productVariantReadPort, CategoryReadPort categoryReadPort) {
        this.orderReadPort = orderReadPort;
        this.orderDetailReadPort = orderDetailReadPort;
        this.productReadPort = productReadPort;
        this.productVariantReadPort = productVariantReadPort;
        this.categoryReadPort = categoryReadPort;
    }

    @Override
    public CompositeStatisticModel getStatistic(LocalDate from, LocalDate to) {
        List<Order> listOrders = this.getListSuccessOrder(from, to);
        Map<String, StatisticModel> statisticByProduct = this.getStatisticByProduct(listOrders);
        Map<String, StatisticModel> statisticByCategory = this.getStatisticByCategory(listOrders);
        int[] totalIncomeAndOrders = getTotalIncomeAndOrders(listOrders);
        return new CompositeStatisticModel(statisticByProduct, statisticByCategory, totalIncomeAndOrders[1], totalIncomeAndOrders[0]);
    }
    private int[] getTotalIncomeAndOrders(List<Order> orders) {
        int[] result = new int[2];
        result[0]= orders.size();
        int totalIncome = 0;
        for(Order o : orders) {
            totalIncome+=o.getTotal();
        }
        result[1]=totalIncome;
        return result;
    }
    private Map<String, StatisticModel> getStatisticByProduct(List<Order> orders) {
        Map<String,StatisticModel> result = new HashMap<>();
        List<OrderDetail> orderDetails = this.getListDetailsFromListOrders(orders);
        for(OrderDetail od: orderDetails) {
            String productName = od.getProductVariant().getName();
            if(result.containsKey(productName)) {
                StatisticModel sm = result.get(productName);
                sm.setProductAmount(sm.getProductAmount()+od.getAmount());
                sm.setTotalPrice(sm.getTotalPrice()+od.getAmount()*od.getUnitPrice());
            }
            else {
                result.put(productName, new StatisticModel(od.getAmount(),od.getAmount()*od.getUnitPrice() ));
            }
        }
        return result;
    }
    private Map<String, StatisticModel> getStatisticByCategory(List<Order> orders) {
        Map<String,StatisticModel> result = new HashMap<>();
        List<OrderDetail> orderDetails = this.getListDetailsFromListOrders(orders);
         for(OrderDetail od: orderDetails) {
            String categoryName = od.getProductVariant().getProduct().getCategory().getName();
            if(result.containsKey(categoryName)) {
                StatisticModel sm = result.get(categoryName);
                sm.setProductAmount(sm.getProductAmount()+od.getAmount());
                sm.setTotalPrice(sm.getTotalPrice()+od.getAmount()*od.getUnitPrice());
            }
            else {
                result.put(categoryName, new StatisticModel(od.getAmount(),od.getAmount()*od.getUnitPrice() ));
            }
        }
        return result;
    }
    private List<Order> getListSuccessOrder(LocalDate from, LocalDate to) {
        Instant fromTime= TimeConverter.getInstantFromStartLocalDate(from);
        Instant toTime = TimeConverter.getInstantFromEndLocalDate(to);
        return orderReadPort.findByStatusAndCreatedAtBetween(EOrderStatus.RECEIVED, fromTime, toTime);
    }
    private List<OrderDetail> getListDetailsFromListOrders(List<Order> listOrders) {
        List<Long> listOrderIds = listOrders.stream().map((item)->item.getId()).toList();
        return orderDetailReadPort.findByOrderIdIn(listOrderIds);
    }
    
}
