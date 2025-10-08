package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.User;
import vn.uit.clothesshop.dto.response.UserBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.UserDetailInfoResponseDto;
import vn.uit.clothesshop.repository.UserRepository;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NotNull
    public List<@NotNull UserBasicInfoResponseDto> handleFindAllUsers() {
        return this.findAllUsers()
                .stream()
                .map((final var user) -> new UserBasicInfoResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole()))
                .toList();
    }

    @NotNull
    public List<@NotNull User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Nullable
    public UserDetailInfoResponseDto handleFindUserById(final long id) {
        final var user = this.findUserById(id);
        if (user == null) {
            return null;
        }

        return new UserDetailInfoResponseDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAvatarFileName(), // TODO: change file name to path
                user.getRole());
    }

    @Nullable
    public User findUserById(final long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Nullable
    private User handleSaveUser(@NotNull final User user) {
        try {
            return this.userRepository.save(user);
        } catch (final Exception exception) {
            log.error("Error saving user", exception);
            return null;
        }
    }
}
