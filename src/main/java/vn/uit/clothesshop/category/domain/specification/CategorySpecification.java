package vn.uit.clothesshop.category.domain.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.uit.clothesshop.category.domain.Category;

public final class CategorySpecification {
    private CategorySpecification() {
    }

    public static Specification<Category> nameLike(@Nullable final String keyword) {
        return (final Root<Category> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Category.Fields.name)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<Category> descLike(@Nullable final String keyword) {
        return (final Root<Category> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Category.Fields.desc)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }
}
