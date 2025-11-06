package vn.uit.clothesshop.user.domain;

import java.util.Optional;

public interface UserAccess {
    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String username);
}
