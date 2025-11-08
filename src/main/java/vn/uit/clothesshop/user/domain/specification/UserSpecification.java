package vn.uit.clothesshop.user.domain.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.uit.clothesshop.user.domain.User;

public final class UserSpecification {
    private UserSpecification() {
    }

    public static Specification<User> usernameLike(@Nullable final String keyword) {
        return (final Root<User> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(User.Fields.username)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<User> firstNameLike(@Nullable final String keyword) {
        return (final Root<User> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(User.Fields.firstName)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<User> lastNameLike(@Nullable final String keyword) {
        return (final Root<User> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(User.Fields.lastName)),
                    "%" + keyword.toLowerCase(Locale.ROOT) + "%");
        };
    }
}
