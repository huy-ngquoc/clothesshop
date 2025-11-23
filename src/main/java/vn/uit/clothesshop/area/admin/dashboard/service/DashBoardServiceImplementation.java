package vn.uit.clothesshop.area.admin.dashboard.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.infra.jpa.repository.OrderRepository;
import vn.uit.clothesshop.util.TimeConverter;

@Service
public class DashBoardServiceImplementation  implements DashBoardService{
    private final OrderRepository orderRepo;
    public DashBoardServiceImplementation(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }
    @Override
    public List<Long> incomeEveryDay(LocalDate from, LocalDate to) {
        List<Long> res = new ArrayList<>();
        while(!from.isAfter(to)) {
            Instant start = TimeConverter.getInstantFromStartLocalDate(from);
            Instant end = TimeConverter.getInstantFromEndLocalDate(from);
            List<Order> orders = orderRepo.findByStatusAndCreatedAtBetween(EOrderStatus.RECEIVED, start, end);
            res.add(calculateTotalValueFromListOrder(orders));
            from.plusDays(1);
        }
        return res;
    }
    private long calculateTotalValueFromListOrder(List<Order> orders) {
        long res=0;
        for(Order order: orders) {
            res=res+order.getTotal();
        }
        return res;
    }

    @Override
    public Long incomeInMonth(int month, int year) {
        LocalDate[] localDates = TimeConverter.getStartAndEndOfMonth(month, year);
        Instant start = TimeConverter.getInstantFromStartLocalDate(localDates[0]);
        Instant end = TimeConverter.getInstantFromEndLocalDate(localDates[1]);
        List<Order> orders= orderRepo.findByStatusAndCreatedAtBetween(EOrderStatus.RECEIVED, start, end);
        return calculateTotalValueFromListOrder(orders);
    }
    
    
}
