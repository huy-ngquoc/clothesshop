package vn.uit.clothesshop.feature.user.domain.port;

import java.util.Optional;

import vn.uit.clothesshop.feature.user.domain.User;

public interface UserReadPort {
    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String username);

    Optional<User> findByEmail(final String email);
}
