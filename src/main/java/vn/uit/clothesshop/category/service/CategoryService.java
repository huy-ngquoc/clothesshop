package vn.uit.clothesshop.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.mapper.CategoryMapper;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateInfoViewModel;
import vn.uit.clothesshop.category.repository.CategoryRepository;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
@Validated
@Slf4j
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final LocalImageStorage storage;
    private final ApplicationEventPublisher eventPublisher;

    public CategoryService(
            final CategoryRepository repository,
            final CategoryMapper mapper,
            final LocalImageStorage storage,
            final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.storage = storage;
        this.eventPublisher = eventPublisher;
    }

    public Page<CategoryBasicInfoViewModel> findAllBasic(@NonNull final Pageable pageable) {
        return this.repository.findAll(pageable).map(this.mapper::toBasicInfo);
    }

    public List<CategoryCardViewModel> findRandomForHomepage(int size) {
        final var list = this.repository.findRandom(PageRequest.of(0, size));
        return list.stream().map(this.mapper::toCard).toList();
    }

    public Optional<CategoryDetailInfoViewModel> findDetailById(final long id) {
        return this.repository.findById(id).map(this.mapper::toDetailInfo);
    }

    public Optional<String> getPathStringById(final long id) {
        return this.repository.findById(id).map(this.mapper::getPathString);
    }

    public long create(@NotNull final CategoryCreationForm form) {
        final var category = this.mapper.toEntityOnCreate(form);
        return this.repository.save(category).getId();
    }

    public Optional<CategoryUpdateInfoViewModel> getUpdateInfoViewModel(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Transactional
    public boolean updateInfo(final long id, @NotNull final CategoryUpdateInfoForm form) {
        final var category = this.repository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        this.mapper.applyUpdateInfo(form, category);
        return true;
    }

    @Transactional
    public boolean updateImage(
            final long id,
            final MultipartFile file) {
        final var category = this.repository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        final var oldAvatarFileName = category.getImage();
        final var newAvatarFileName = this.storage.handleSaveUploadFile(file, ImageFolder.CATEGORY.sub());
        if (!StringUtils.hasText(newAvatarFileName)) {
            return false;
        }

        category.setImage(newAvatarFileName);
        this.eventPublisher.publishEvent(
                new ImageReplaced(oldAvatarFileName, newAvatarFileName, ImageFolder.CATEGORY));
        return true;
    }

    @Transactional
    public boolean deleteImage(final long id) {
        final var category = this.repository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        var avatarFile = category.getImage();
        if (!StringUtils.hasText(avatarFile)) {
            return true;
        }

        category.setImage(null);

        eventPublisher.publishEvent(new ImageDeleted(avatarFile, ImageFolder.CATEGORY));
        return true;
    }

    public void deleteById(final long id) {
        this.repository.deleteById(id);
    }
}
