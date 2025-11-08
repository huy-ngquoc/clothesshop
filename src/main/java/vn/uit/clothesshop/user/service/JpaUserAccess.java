package vn.uit.clothesshop.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.user.domain.User;
import vn.uit.clothesshop.user.domain.UserAccess;
import vn.uit.clothesshop.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
class JpaUserAccess implements UserAccess {

    private final UserRepository repository;

    public JpaUserAccess(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(final long id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return this.repository.findByUsername(username);
    }

}
