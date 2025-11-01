package vn.uit.clothesshop.category.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationRequestDto;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoRequestDto;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryBasicInfoResponseDto;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryDetailInfoResponseDto;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryHomepageInfoResponseDto;
import vn.uit.clothesshop.category.presentation.viewmodel.CategoryUpdateInfoMiddleDto;
import vn.uit.clothesshop.category.repository.CategoryRepository;
import vn.uit.clothesshop.service.ImageFileService;

@Service
@Validated
@Slf4j
public class CategoryService {
    private static final String IMAGE_SUB_FOLDER_NAME = "category";

    @NotNull
    private final CategoryRepository categoryRepository;

    @NotNull
    private final ImageFileService imageFileService;

    public CategoryService(
            @NotNull final CategoryRepository categoryRepo,
            @NotNull final ImageFileService imageFileService) {
        this.categoryRepository = categoryRepo;
        this.imageFileService = imageFileService;
    }

    @NotNull
    public List<@NotNull CategoryBasicInfoResponseDto> handleFindAll() {
        return this.findAll().stream()
                .map((final Category category) -> new CategoryBasicInfoResponseDto(
                        category.getId(),
                        category.getName(),
                        category.getDesc()))
                .toList();
    }

    @NotNull
    public List<@NotNull Category> findAll() {
        return categoryRepository.findAll();
    }

    @NotNull
    public Page<CategoryHomepageInfoResponseDto> handleFindRadomForHomepage(
            @NotNull final Pageable pageable) {
        return this.findRandom(pageable).map(
                (final Category category) -> new CategoryHomepageInfoResponseDto(
                        category.getId(),
                        category.getName(),
                        category.getDesc(),
                        this.imageFileService.getPathString(category.getImage(), IMAGE_SUB_FOLDER_NAME)));
    }

    @NotNull
    public Page<Category> findRandom(@NotNull final Pageable pageable) {
        return this.categoryRepository.findRandom(pageable);
    }

    @Nullable
    public CategoryDetailInfoResponseDto handleFindById(final long id) {
        final var category = this.findById(id);
        if (category == null) {
            return null;
        }

        return new CategoryDetailInfoResponseDto(
                category.getName(),
                category.getDesc(),
                this.imageFileService.getPathString(category.getImage(), IMAGE_SUB_FOLDER_NAME),
                category.getAmountOfProduct());
    }

    @Nullable
    public Category findById(final long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @NotNull
    public Category getReferenceById(final long id) {
        return this.categoryRepository.getReferenceById(id);
    }

    @Nullable
    public String findImageFilePathOfCategoryById(final long id) {
        final var category = this.findById(id);
        if (category == null) {
            return null;
        }

        return this.imageFileService.getPathString(category.getImage(), IMAGE_SUB_FOLDER_NAME);
    }

    @Nullable
    public Long handleCreateCategory(@NotNull final CategoryCreationRequestDto requestDto) {
        final var category = new Category(
                requestDto.getName(),
                requestDto.getDesc());

        final var savedCategory = this.handleSaveCategory(category);
        if (savedCategory == null) {
            return null;
        }

        return savedCategory.getId();
    }

    @Nullable
    public CategoryUpdateInfoMiddleDto handleCreateMiddleDtoForUpdate(final long id) {
        final var category = this.findById(id);
        if (category == null) {
            return null;
        }

        final var requestDto = new CategoryUpdateInfoRequestDto(
                category.getName(),
                category.getDesc());
        final var imageFilePath = this.imageFileService.getPathString(category.getImage(), IMAGE_SUB_FOLDER_NAME);

        return new CategoryUpdateInfoMiddleDto(imageFilePath, requestDto);
    }

    public boolean handleUpdateCategory(
            final long id,
            @NotNull final CategoryUpdateInfoRequestDto requestDto) {
        final var category = this.findById(id);
        if (category == null) {
            return false;
        }

        category.setName(requestDto.getName());
        category.setDesc(requestDto.getDesc());

        return this.handleSaveCategory(category) != null;
    }

    public boolean handleUpdateCategoryImage(
            final long id,
            final MultipartFile file) {
        final var category = this.findById(id);
        if (category == null) {
            return false;
        }

        var avatarFile = category.getImage();
        if (!StringUtils.hasText(avatarFile)) {
            avatarFile = this.imageFileService.handleSaveUploadFile(file, IMAGE_SUB_FOLDER_NAME);
        } else {
            avatarFile = this.imageFileService.handleUpdateUploadFile(avatarFile, file, IMAGE_SUB_FOLDER_NAME);
        }

        if (!StringUtils.hasText(avatarFile)) {
            return false;
        }

        category.setImage(avatarFile);
        if (this.handleSaveCategory(category) == null) {
            this.imageFileService.handleDeleteUploadFile(avatarFile, IMAGE_SUB_FOLDER_NAME);
            return false;
        }

        return true;
    }

    public boolean handleUpdateCategoryImageDeletion(final long id) {
        final var category = this.findById(id);
        if (category == null) {
            return false;
        }

        var avatarFile = category.getImage();
        if (!StringUtils.hasText(avatarFile)) {
            return true;
        }

        category.setImage(null);

        if (this.handleSaveCategory(category) == null) {
            return false;
        }
        this.imageFileService.handleDeleteUploadFile(avatarFile, IMAGE_SUB_FOLDER_NAME);

        return true;
    }

    public void deleteCategoryById(final long id) {
        this.categoryRepository.deleteById(id);
    }

    @Transactional
    public boolean increaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.increaseAmountOfProduct(id, amount) > 0;
    }

    @Transactional
    public boolean decreaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.decreaseAmountOfProduct(id, amount) > 0;
    }

    @Nullable
    private Category handleSaveCategory(@NotNull final Category category) {
        try {
            return categoryRepository.save(category);
        } catch (final Exception exception) {
            log.error("Error saving Category", exception);
            return null;
        }
    }
}
