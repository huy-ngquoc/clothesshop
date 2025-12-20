package vn.uit.clothesshop.feature.recommendation.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.clothesshop.feature.product.domain.Product;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompositeResponse {
    private Model model;
    private List<Product> products;
}
