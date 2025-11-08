package vn.uit.clothesshop.product.domain.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;

public final class ProductVariantSpecification {
    public static Specification<ProductVariant> hasProductId(final long productId) {
        return (final Root<ProductVariant> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var productIdPath = root
                    .get(ProductVariant.Fields.product)
                    .get(Product.Fields.id);
            return criteriaBuilder.equal(productIdPath, productId);
        };
    }

    public static Specification<ProductVariant> colorLike(final String keyword) {
        return (final Root<ProductVariant> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(ProductVariant.Fields.color)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<ProductVariant> sizeLike(final String keyword) {
        return (final Root<ProductVariant> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(ProductVariant.Fields.size)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }
}
