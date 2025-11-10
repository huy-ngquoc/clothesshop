package vn.uit.clothesshop.area.admin.user.service;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.infra.jpa.repository.UserRepository;
import vn.uit.clothesshop.feature.user.mapper.UserMapper;
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
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
@Transactional(readOnly = true)
class DefaultUserService implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final LocalImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;

    public DefaultUserService(
            final UserRepository repository,
            final UserMapper mapper,
            final LocalImageStorage imageStorage,
            final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.imageStorage = imageStorage;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Page<UserBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<User> spec,
            @NonNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toBasicInfoViewModel);
    }

    @Override
    public Optional<UserDetailInfoViewModel> findDetailById(final long id) {
        return this.repository.findById(id).map(this.mapper::toDetailInfoViewModel);
    }

    @Override
    @Transactional
    public long create(@NonNull final UserCreationForm form) {
        final var user = this.mapper.toEntityOnCreate(form);
        return this.repository.save(user).getId();
    }

    @Override
    public Optional<Pair<UserInfoUpdateViewModel, UserInfoUpdateForm>> findInfoUpdateById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Override
    public Optional<UserInfoUpdateViewModel> findInfoUpdateViewModelById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfoViewModel);
    }

    @Override
    @Transactional
    public void updateInfoById(
            final long id,
            @NonNull final UserInfoUpdateForm form) {
        final var user = this.repository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        this.mapper.applyUpdateInfo(user, form);
    }

    @Override
    public Optional<UserPasswordUpdateForm> findPasswordUpdateFormById(final long id) {
        if (!this.repository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(new UserPasswordUpdateForm());
    }

    @Override
    @Transactional
    public void updatePasswordById(
            final long id,
            @NonNull final UserPasswordUpdateForm form) {
        final var user = this.repository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        this.mapper.applyUpdatePassword(user, form);
    }

    @Override
    public Optional<Pair<UserAvatarUpdateViewModel, UserAvatarUpdateForm>> findAvatarUpdateById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateAvatar);
    }

    @Override
    public Optional<UserAvatarUpdateViewModel> findAvatarUpdateViewModelById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateAvatarViewModel);
    }

    @Override
    @Transactional
    public boolean updateAvatarById(
            final long id,
            @NonNull final UserAvatarUpdateForm form) {
        final var user = this.repository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        final var newImageFileName = this.imageStorage.handleSaveUploadFile(
                form.getAvatarFile(),
                ImageFolder.USER.sub());
        if (!StringUtils.hasText(newImageFileName)) {
            return false;
        }

        final var oldAvatarFileName = user.getAvatarFileName();
        user.setAvatarFileName(newImageFileName);
        this.eventPublisher.publishEvent(
                new ImageReplaced(
                        oldAvatarFileName,
                        newImageFileName,
                        ImageFolder.USER));

        return true;
    }

    @Override
    public Optional<UserAvatarDeletionViewModel> findAvatarDeletionById(final long id) {
        return this.repository.findById(id).map(this.mapper::toAvatarDeletion);
    }

    @Override
    @Transactional
    public void deleteAvatarById(final long id) {
        final var user = this.repository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        final var imageFileName = user.getAvatarFileName();
        if (!StringUtils.hasText(imageFileName)) {
            return;
        }

        user.setAvatarFileName(null);
        eventPublisher.publishEvent(new ImageDeleted(imageFileName, ImageFolder.USER));
    }

    @Override
    public Optional<UserDeletionViewModel> findDeletionViewModelById(final long id) {
        return this.repository.findById(id).map(this.mapper::toDeletion);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }
}
