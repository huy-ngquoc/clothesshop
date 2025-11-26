package vn.uit.clothesshop.shared.util;

import org.hibernate.Hibernate;

public final class HibernateUtils {
    private HibernateUtils() {
    }

    public static <T> T unproxy(T entity) {
        if (entity == null) {
            return null;
        }

        return (T) Hibernate.unproxy(entity);
    }
}
