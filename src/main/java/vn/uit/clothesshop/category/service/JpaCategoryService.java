package vn.uit.clothesshop.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.mapper.CategoryMapper;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateImageForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateImageViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDeleteImageViewModel;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateInfoViewModel;
import vn.uit.clothesshop.category.repository.CategoryRepository;
import vn.uit.clothesshop.exception.NotFoundException;
import vn.uit.clothesshop.infrastructure.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
@Validated
@Transactional(readOnly = true)
@Slf4j
class JpaCategoryService implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final LocalImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;

    public JpaCategoryService(
            final CategoryRepository repository,
            final CategoryMapper mapper,
            final LocalImageStorage imageStorage,
            final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.imageStorage = imageStorage;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Page<CategoryBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public List<CategoryCardViewModel> findRandomForHomepage(int size) {
        final var list = this.repository.findRandom(PageRequest.of(0, size));
        return list.stream().map(this.mapper::toCard).toList();
    }

    @Override
    public Optional<CategoryDetailInfoViewModel> findDetailById(final long id) {
        return this.repository.findById(id).map(this.mapper::toDetailInfo);
    }

    @Override
    @Transactional
    public long create(@NotNull final CategoryCreationForm form) {
        final var category = this.mapper.toEntityOnCreate(form);
        return this.repository.save(category).getId();
    }

    @Override
    public Optional<Pair<CategoryUpdateInfoViewModel, CategoryUpdateInfoForm>> findInfoUpdateById(
            final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Override
    public Optional<CategoryUpdateInfoViewModel> findInfoUpdateViewModelById(final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateInfoViewModel);
    }

    @Override
    @Transactional
    public void updateInfoById(final long id, @NotNull final CategoryUpdateInfoForm form) {
        final var category = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        this.mapper.applyUpdateInfo(category, form);
    }

    @Override
    public Optional<Pair<CategoryUpdateImageViewModel, CategoryUpdateImageForm>> findImageUpdateById(
            final long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateImage);
    }

    @Override
    public Optional<CategoryUpdateImageViewModel> findImageUpdateViewModelById(long id) {
        return this.repository.findById(id).map(this.mapper::toUpdateImageViewModel);
    }

    @Override
    @Transactional
    public boolean updateImageById(final long id, final CategoryUpdateImageForm form) {
        final var category = this.repository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        final var newImageFileName = this.imageStorage.handleSaveUploadFile(
                form.getImageFile(),
                ImageFolder.CATEGORY.sub());
        if (!StringUtils.hasText(newImageFileName)) {
            return false;
        }

        final var oldImageFileName = category.getImage();
        category.setImage(newImageFileName);
        this.eventPublisher.publishEvent(
                new ImageReplaced(
                        oldImageFileName,
                        newImageFileName,
                        ImageFolder.CATEGORY));
        return true;
    }

    @Override
    public Optional<CategoryDeleteImageViewModel> findImageDeleteViewModelById(long id) {
        return this.repository.findById(id).map(this.mapper::toDeleteImageViewModel);
    }

    @Override
    @Transactional
    public void deleteImageById(final long id) {
        final var category = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        final var imageFile = category.getImage();
        if (!StringUtils.hasText(imageFile)) {
            return;
        }

        category.setImage(null);
        eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var category = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        final var imageFile = category.getImage();
        if (!StringUtils.hasText(imageFile)) {
            eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));
        }

        this.repository.delete(category);
    }

}
