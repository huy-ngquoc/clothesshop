package vn.uit.clothesshop.feature.user.infra.jpa.adapter;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;
import vn.uit.clothesshop.feature.user.domain.port.UserWritePort;
import vn.uit.clothesshop.feature.user.infra.jpa.repository.UserRepository;

@Repository
class JpaUserAdapter implements UserReadPort, UserWritePort {

    private final UserRepository repository;

    public JpaUserAdapter(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(final long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(final String username) {
        return this.repository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    @Transactional
    @NonNull
    public User save(@NonNull User user) {
        return this.repository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(@NonNull User user) {
        this.repository.delete(user);
    }

}
