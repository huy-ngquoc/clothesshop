package vn.uit.clothesshop.area.site.homepage.mapper;

import vn.uit.clothesshop.area.site.homepage.presentation.ProductClientBasicInfoViewModel;
import vn.uit.clothesshop.feature.product.domain.Product;

public class ProductMapper {
    public static ProductClientBasicInfoViewModel getInfoFromProduct(Product p) {
        return new ProductClientBasicInfoViewModel(p.getId(), p.getName(), p.getShortDesc(), p.getImage(), p.getMinPrice(), p.getMaxPrice(), p.getSold());
    }
}
