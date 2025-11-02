package vn.uit.clothesshop.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    @NotNull
    Optional<User> findByUsername(final String username);

    @NotNull
    Optional<User> findByEmail(final String email);
}
