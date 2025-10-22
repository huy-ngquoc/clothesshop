package vn.uit.clothesshop.specification;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;

public final class ProductSpecification {
    private ProductSpecification() {
    }

    @NotNull
    public static Specification<Product> nameLike(@Nullable final String keyword) {
        return (final Root<Product> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Product.Fields.name)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }

    @NotNull
    public static Specification<Product> priceBetween(
            @Nullable final Integer from,
            @Nullable final Integer to) {
        return (final Root<Product> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var minPricePath = root.<Integer>get(Product.Fields.minPrice);

            if (from == null) {
                if (to == null) {
                    return criteriaBuilder.conjunction();
                }

                return criteriaBuilder.lessThanOrEqualTo(minPricePath, to);
            }

            if (to == null) {
                return criteriaBuilder.greaterThanOrEqualTo(minPricePath, from);
            }

            return criteriaBuilder.between(minPricePath, from, to);
        };
    }

    public static Specification<Product> anyColors(@Nullable final Set<@NotBlank String> listColors) {
        return (final Root<Product> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if ((listColors == null) || (listColors.isEmpty())) {
                return criteriaBuilder.conjunction();
            }

            final var colorVariantPath = root
                    .<Product, ProductVariant>join(Product.Fields.variants, JoinType.INNER)
                    .<String>get(ProductVariant.Fields.color);
            final var colorVariantIn = criteriaBuilder.in(colorVariantPath);
            listColors.forEach(colorVariantIn::value);

            return colorVariantIn;
        };
    }

    public static Specification<Product> anySizes(@Nullable final Set<String> listSizes) {
        return (final Root<Product> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if ((listSizes == null) || (listSizes.isEmpty())) {
                return criteriaBuilder.conjunction();
            }

            final var sizeVariantPath = root
                    .<Product, ProductVariant>join(Product.Fields.variants, JoinType.INNER)
                    .<String>get(ProductVariant.Fields.size);
            final var sizeVariantIn = criteriaBuilder.in(sizeVariantPath);
            listSizes.forEach(sizeVariantIn::value);

            return sizeVariantIn;
        };
    }
}
