package vn.uit.clothesshop.feature.user.mapper;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.presentation.form.RegisterDto;
import vn.uit.clothesshop.feature.user.presentation.form.UserAvatarUpdateForm;
import vn.uit.clothesshop.feature.user.presentation.form.UserCreationForm;
import vn.uit.clothesshop.feature.user.presentation.form.UserInfoUpdateForm;
import vn.uit.clothesshop.feature.user.presentation.form.UserPasswordUpdateForm;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserAvatarDeletionViewModel;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserAvatarUpdateViewModel;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserBasicInfoViewModel;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserDeletionViewModel;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserDetailInfoViewModel;
import vn.uit.clothesshop.feature.user.presentation.viewmodel.UserInfoUpdateViewModel;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;

@Component
public class UserMapper {
    private final LocalImageStorage storage;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(
            final LocalImageStorage storage,
            final PasswordEncoder passwordEncoder) {
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserFromRegisterDto(RegisterDto registerDto) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getFirstName() + " " + registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setHashedPassword(hashedPassword);
        user.setPhoneNumber(registerDto.getPhone());
        return user;
    }

    public UserBasicInfoViewModel toBasicInfoViewModel(final User user) {
        return new UserBasicInfoViewModel(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole());
    }

    public UserDetailInfoViewModel toDetailInfoViewModel(final User user) {
        return new UserDetailInfoViewModel(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                this.getPathString(user),
                user.getRole());
    }

    @NonNull
    public User toEntityOnCreate(@NonNull final UserCreationForm form) {
        return new User(
                form.getUsername(),
                this.passwordEncoder.encode(form.getPassword()),
                form.getFirstName(),
                form.getLastName(),
                form.getEmail(),
                form.getPhoneNumber(),
                form.getRole());
    }

    @NonNull
    public Pair<UserInfoUpdateViewModel, UserInfoUpdateForm> toUpdateInfo(@NonNull final User user) {
        return Pair.of(
                this.toUpdateInfoViewModel(user),
                this.toUpdateInfoForm(user));
    }

    @NonNull
    public UserInfoUpdateViewModel toUpdateInfoViewModel(@NonNull final User user) {
        return new UserInfoUpdateViewModel(
                user.getUsername(),
                this.getPathString(user));
    }

    @NonNull
    public UserInfoUpdateForm toUpdateInfoForm(@NonNull final User user) {
        return new UserInfoUpdateForm(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole());
    }

    public void applyUpdateInfo(
            @NonNull final User user,
            @NonNull final UserInfoUpdateForm form) {
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setRole(form.getRole());
    }

    public void applyUpdatePassword(
            @NonNull final User user,
            @NonNull final UserPasswordUpdateForm form) {
        user.setHashedPassword(this.passwordEncoder.encode(form.getNewPassword()));
    }

    @NonNull
    public Pair<UserAvatarUpdateViewModel, UserAvatarUpdateForm> toUpdateAvatar(@NonNull final User user) {
        return Pair.of(
                this.toUpdateAvatarViewModel(user),
                this.toUpdateAvatarForm(user));
    }

    @NonNull
    public UserAvatarUpdateViewModel toUpdateAvatarViewModel(@NonNull final User user) {
        return new UserAvatarUpdateViewModel(
                this.getPathString(user));
    }

    @NonNull
    public UserAvatarUpdateForm toUpdateAvatarForm(@NonNull final User user) {
        return new UserAvatarUpdateForm();
    }

    @NonNull
    public UserAvatarDeletionViewModel toAvatarDeletion(@NonNull final User user) {
        return new UserAvatarDeletionViewModel(
                this.getPathString(user));
    }

    @NonNull
    public UserDeletionViewModel toDeletion(@NonNull final User user) {
        return new UserDeletionViewModel(
                this.getPathString(user));
    }

    public String getPathString(@NotNull final User e) {
        return this.getPathString(e.getAvatarFileName());
    }

    public String getPathString(@Nullable final String fileName) {
        return this.storage.getPathString(fileName, ImageFolder.USER.sub());
    }
}
