package vn.uit.clothesshop.area.site.homepage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.area.site.homepage.mapper.ProductMapper;
import vn.uit.clothesshop.area.site.homepage.presentation.ProductClientBasicInfoViewModel;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;

@Service
public class ProductClientServiceImplementation implements ProductClientService {
    private final ProductReadPort productReadPort;
    public ProductClientServiceImplementation(ProductReadPort productReadPort) {
        this.productReadPort = productReadPort;
    }
    @Override
    public Page<ProductClientBasicInfoViewModel> findAllBasic(Specification<Product> spec, Pageable pageable) {
        Page<Product> pageProducts = productReadPort.findAll(spec, pageable);
        return pageProducts.map((item)->{
            return ProductMapper.getInfoFromProduct(item);
        });
    }

}
