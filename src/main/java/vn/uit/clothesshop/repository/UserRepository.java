package vn.uit.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
