package vn.uit.clothesshop.feature.order.domain.port;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.order.domain.Order;

public interface OrderWritePort {

    @NonNull
    Order save(@NonNull final Order order);

    void deleteById(long id);

    void delete(@NonNull Order order);
}
