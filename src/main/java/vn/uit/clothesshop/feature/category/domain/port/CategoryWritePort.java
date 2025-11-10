package vn.uit.clothesshop.feature.category.domain.port;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.category.domain.Category;

public interface CategoryWritePort {
    @NonNull
    Category save(@NonNull final Category category);

    void deleteById(long id);

    void delete(@NonNull Category category);
}
