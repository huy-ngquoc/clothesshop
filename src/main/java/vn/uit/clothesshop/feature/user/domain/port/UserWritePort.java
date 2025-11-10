package vn.uit.clothesshop.feature.user.domain.port;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.user.domain.User;

public interface UserWritePort {
    @NonNull
    User save(@NonNull User user);

    void deleteById(long id);

    void delete(@NonNull User user);
}
