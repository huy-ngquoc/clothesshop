package vn.uit.clothesshop.feature.user.infra.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.feature.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    @NotNull
    Optional<User> findByUsername(final String username);

    @NotNull
    Optional<User> findByEmail(final String email);
}
