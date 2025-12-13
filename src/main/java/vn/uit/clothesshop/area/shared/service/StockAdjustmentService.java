package vn.uit.clothesshop.area.shared.service;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderDetailSpecification;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;

@Service
@RequiredArgsConstructor
public class StockAdjustmentService {
    private final OrderDetailReadPort orderDetailReadPort;
    private final ProductVariantReadPort productVariantReadPort;
    private final ProductReadPort productReadPort;
    private final ProductVariantWritePort productVariantWritePort;
    private final ProductWritePort productWritePort;

    @Transactional
    public void adjustStockForOrder(long orderId, StockStrategy strategy) {
        var page = 0;
        var size = PagingConstraint.MAX_SIZE;

        final var orderDetailSpec = OrderDetailSpecification.orderIdEquals(orderId);
        var orderDetailPage = orderDetailReadPort.findAll(orderDetailSpec, PageRequest.of(page, size));

        final var variantMap = new HashMap<Long, ProductVariant>();
        final var productMap = new HashMap<Long, Product>();

        while (!orderDetailPage.isEmpty()) {
            final var variantsToUpdate = new ArrayList<ProductVariant>();
            final var productsToUpdate = new ArrayList<Product>();

            for (final var orderDetail : orderDetailPage.getContent()) {
                final var productVariantId = orderDetail.getProductVariantId();

                var productVariant = variantMap.computeIfAbsent(productVariantId,
                        id -> productVariantReadPort.findById(id)
                                .orElseThrow(() -> new NotFoundException("Product Variant not found: " + id)));

                final var productId = productVariant.getProductId();
                var product = productMap.computeIfAbsent(productId, id -> productReadPort.findById(id)
                        .orElseThrow(() -> new NotFoundException("Product not found: " + id)));

                strategy.apply(productVariant, product, orderDetail.getAmount());

                variantsToUpdate.add(productVariant);
                productsToUpdate.add(product);
            }

            if (!variantsToUpdate.isEmpty()) {
                this.productVariantWritePort.saveAll(variantsToUpdate);
            }
            if (!productsToUpdate.isEmpty()) {
                this.productWritePort.saveAll(productsToUpdate);
            }

            ++page;
            orderDetailPage = orderDetailReadPort.findAll(orderDetailSpec, PageRequest.of(page, size));
        }
    }

    @FunctionalInterface
    public interface StockStrategy {
        void apply(ProductVariant variant, Product product, int amount);
    }
}
