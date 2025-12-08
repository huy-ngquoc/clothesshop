package vn.uit.clothesshop.area.admin.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.area.admin.category.mapper.CategoryAdminMapper;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminCreationForm;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.category.presentation.form.CategoryAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminDetailInfoViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminImageDeleteViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminImageUpdateViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryAdminInfoUpdateViewModel;
import vn.uit.clothesshop.area.admin.category.presentation.viewmodel.CategoryCardViewModel;
import vn.uit.clothesshop.area.shared.exception.NotFoundException;
import vn.uit.clothesshop.feature.category.domain.Category;
import vn.uit.clothesshop.feature.category.domain.port.CategoryReadPort;
import vn.uit.clothesshop.feature.category.domain.port.CategoryWritePort;
import vn.uit.clothesshop.infra.storage.LocalImageStorage;
import vn.uit.clothesshop.shared.storage.ImageFolder;
import vn.uit.clothesshop.shared.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.storage.event.ImageReplaced;

@Service
@Transactional(readOnly = true)
@Slf4j
class DefaultCategoryAdminService implements CategoryAdminService {
    private final CategoryReadPort categoryReadPort;
    private final CategoryWritePort categoryWritePort;
    private final CategoryAdminMapper mapper;
    private final LocalImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;

    public DefaultCategoryAdminService(
            final CategoryReadPort categoryReadPort,
            final CategoryWritePort categoryWritePort,
            final CategoryAdminMapper mapper,
            final LocalImageStorage imageStorage,
            final ApplicationEventPublisher eventPublisher) {
        this.categoryReadPort = categoryReadPort;
        this.categoryWritePort = categoryWritePort;
        this.mapper = mapper;
        this.imageStorage = imageStorage;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Page<CategoryAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable) {
        return this.categoryReadPort.findAll(spec, pageable).map(this.mapper::toBasicInfo);
    }

    @Override
    public List<CategoryCardViewModel> findRandomForHomepage(int size) {
        final var list = this.categoryReadPort.findRandom(size);
        return list.stream().map(this.mapper::toCard).toList();
    }

    @Override
    public Optional<CategoryAdminDetailInfoViewModel> findDetailById(final long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toDetailInfo);
    }

    @Override
    @Transactional
    public long create(@NonNull final CategoryAdminCreationForm form) {
        final var category = this.mapper.toEntityOnCreate(form);
        return this.categoryWritePort.save(category).getId();
    }

    @Override
    public Optional<Pair<CategoryAdminInfoUpdateViewModel, CategoryAdminInfoUpdateForm>> findInfoUpdateById(
            final long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toUpdateInfo);
    }

    @Override
    public Optional<CategoryAdminInfoUpdateViewModel> findInfoUpdateViewModelById(final long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toUpdateInfoViewModel);
    }

    @Override
    @Transactional
    public void updateInfoById(final long id, @NonNull final CategoryAdminInfoUpdateForm form) {
        final var category = this.categoryReadPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        this.mapper.applyUpdateInfo(category, form);
        this.categoryWritePort.save(category);
    }

    @Override
    public Optional<Pair<CategoryAdminImageUpdateViewModel, CategoryAdminImageUpdateForm>> findImageUpdateById(
            final long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toUpdateImage);
    }

    @Override
    public Optional<CategoryAdminImageUpdateViewModel> findImageUpdateViewModelById(long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toUpdateImageViewModel);
    }

    @Override
    @Transactional
    public boolean updateImageById(final long id, final CategoryAdminImageUpdateForm form) {
        final var category = this.categoryReadPort.findById(id).orElse(null);
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
    public Optional<CategoryAdminImageDeleteViewModel> findImageDeleteViewModelById(long id) {
        return this.categoryReadPort.findById(id).map(this.mapper::toDeleteImageViewModel);
    }

    @Override
    @Transactional
    public void deleteImageById(final long id) {
        final var category = this.categoryReadPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        final var imageFile = category.getImage();
        if (!StringUtils.hasText(imageFile)) {
            return;
        }

        category.setImage(null);
        eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));

        this.categoryWritePort.save(category);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        final var category = this.categoryReadPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        final var imageFile = category.getImage();
        if (!StringUtils.hasText(imageFile)) {
            eventPublisher.publishEvent(new ImageDeleted(imageFile, ImageFolder.CATEGORY));
        }

        this.categoryWritePort.delete(category);
    }

}
