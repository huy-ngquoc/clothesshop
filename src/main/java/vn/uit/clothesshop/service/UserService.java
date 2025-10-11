package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.User;
import vn.uit.clothesshop.dto.middle.UserUpdateAvatarMiddleDto;
import vn.uit.clothesshop.dto.middle.UserUpdateInfoMiddleDto;
import vn.uit.clothesshop.dto.request.UserCreationRequestDto;
import vn.uit.clothesshop.dto.request.UserUpdateAvatarRequestDto;
import vn.uit.clothesshop.dto.request.UserUpdateInfoRequestDto;
import vn.uit.clothesshop.dto.request.UserUpdatePasswordRequestDto;
import vn.uit.clothesshop.dto.response.UserBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.UserDetailInfoResponseDto;
import vn.uit.clothesshop.repository.UserRepository;

@Service
@Slf4j
public class UserService {
    private static final String IMAGE_SUB_FOLDER_NAME = "user";

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final ImageUploadService imageUploadService;

    @NotNull
    private final PasswordEncoder passwordEncoder;

    public UserService(
            final UserRepository userRepository,
            final ImageUploadService imageUploadService,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageUploadService = imageUploadService;
        this.passwordEncoder = passwordEncoder;
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

        final var avatarFilePath = this.imageUploadService.getPathString(
                user.getAvatarFileName(),
                IMAGE_SUB_FOLDER_NAME);

        return new UserDetailInfoResponseDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                avatarFilePath,
                user.getRole());
    }

    @Nullable
    public User findUserById(final long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Nullable
    public Long handleCreateUser(@NotNull final UserCreationRequestDto requestDto) {
        final var hashedPassword = this.passwordEncoder.encode(requestDto.getPassword());

        final var user = new User(
                requestDto.getUsername(),
                hashedPassword,
                requestDto.getFirstName(),
                requestDto.getLastName(),
                requestDto.getEmail(),
                requestDto.getPhoneNumber(),
                requestDto.getRole());

        final var savedUser = this.handleSaveUser(user);
        if (savedUser == null) {
            return null;
        }

        return savedUser.getId();
    }

    @Nullable
    public UserUpdateInfoMiddleDto handleCreateMiddleDtoForUpdateInfo(final long id) {
        final var user = this.findUserById(id);
        if (user == null) {
            return null;
        }

        final var requestDto = new UserUpdateInfoRequestDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole());

        return new UserUpdateInfoMiddleDto(
                user.getUsername(),
                this.imageUploadService.getPathString(user.getAvatarFileName(), IMAGE_SUB_FOLDER_NAME),
                requestDto);
    }

    @Nullable
    public UserUpdateAvatarMiddleDto handleCreateMiddleDtoForUpdateAvatar(final long id) {
        final var user = this.findUserById(id);
        if (user == null) {
            return null;
        }

        return new UserUpdateAvatarMiddleDto(
                this.imageUploadService.getPathString(user.getAvatarFileName(), IMAGE_SUB_FOLDER_NAME),
                new UserUpdateAvatarRequestDto());
    }

    public boolean handleUpdateUserInfo(
            final long id,
            @NotNull final UserUpdateInfoRequestDto requestDto) {
        final var user = this.findUserById(id);
        if (user == null) {
            return false;
        }

        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setRole(requestDto.getRole());

        return this.handleSaveUser(user) != null;
    }

    public boolean handleUpdateUserPassword(
            final long id,
            @NotNull final UserUpdatePasswordRequestDto requestDto) {
        final var user = this.findUserById(id);
        if (user == null) {
            return false;
        }

        final var newHashedPassword = this.passwordEncoder.encode(requestDto.getNewPassword());
        user.setHashedPassword(newHashedPassword);

        return this.handleSaveUser(user) != null;
    }

    public boolean handleUpdateUserAvatar(
            final long id,
            final MultipartFile file) {
        final var user = this.findUserById(id);
        if (user == null) {
            return false;
        }

        var avatarFile = user.getAvatarFileName();
        if (!StringUtils.hasText(avatarFile)) {
            avatarFile = this.imageUploadService.handleSaveUploadFile(file, IMAGE_SUB_FOLDER_NAME);
        } else {
            avatarFile = this.imageUploadService.handleUpdateUploadFile(avatarFile, file, IMAGE_SUB_FOLDER_NAME);
        }

        if (!StringUtils.hasText(avatarFile)) {
            return false;
        }

        user.setAvatarFileName(avatarFile);
        if (this.handleSaveUser(user) == null) {
            this.imageUploadService.handleDeleteUploadFile(avatarFile, IMAGE_SUB_FOLDER_NAME);
            return false;
        }

        return true;
    }

    public void deleteUserById(final long id) {
        this.userRepository.deleteById(id);
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
