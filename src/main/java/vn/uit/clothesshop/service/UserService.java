package vn.uit.clothesshop.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.User;
import vn.uit.clothesshop.repository.UserRepository;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Nullable
    public User findUserById(final long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Nullable
    public User saveUser(@NotNull final User user) {
        try {
            return this.userRepository.save(user);
        } catch (final Exception exception) {
            log.error("Error saving user", exception);
            return null;
        }
    }
}
