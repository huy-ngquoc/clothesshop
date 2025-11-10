package vn.uit.clothesshop.area.admin.user.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.feature.user.domain.User;
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

public interface UserService {
    default Page<UserBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<UserBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<User> spec,
            @NonNull final Pageable pageable);

    Optional<UserDetailInfoViewModel> findDetailById(final long id);

    long create(@NonNull final UserCreationForm form);

    Optional<Pair<UserInfoUpdateViewModel, UserInfoUpdateForm>> findInfoUpdateById(final long id);

    Optional<UserInfoUpdateViewModel> findInfoUpdateViewModelById(final long id);

    void updateInfoById(
            final long id,
            @NonNull final UserInfoUpdateForm form);

    Optional<UserPasswordUpdateForm> findPasswordUpdateFormById(final long id);

    void updatePasswordById(
            final long id,
            @NonNull final UserPasswordUpdateForm form);

    Optional<Pair<UserAvatarUpdateViewModel, UserAvatarUpdateForm>> findAvatarUpdateById(final long id);

    Optional<UserAvatarUpdateViewModel> findAvatarUpdateViewModelById(final long id);

    boolean updateAvatarById(
            final long id,
            @NonNull final UserAvatarUpdateForm form);

    Optional<UserAvatarDeletionViewModel> findAvatarDeletionById(final long id);

    void deleteAvatarById(final long id);

    Optional<UserDeletionViewModel> findDeletionViewModelById(final long id);

    void deleteById(final long id);

    Optional<User> findByEmail(String email);
}
