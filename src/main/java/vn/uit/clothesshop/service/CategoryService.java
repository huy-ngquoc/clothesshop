package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.dto.response.CategoryHomepageInfoResponseDto;
import vn.uit.clothesshop.repository.CategoryRepository;

@Service
@Validated
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

    @Nullable
    public Category findById(final long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @NotNull
    public Page<CategoryHomepageInfoResponseDto> handleFindRadomForHomepage(
            @NotNull final Pageable pageable) {
        return this.findRandom(pageable).map(
                (final Category category) -> new CategoryHomepageInfoResponseDto(
                        category.getId(),
                        category.getName(),
                        this.imageFileService.getPathString(category.getImage(), IMAGE_SUB_FOLDER_NAME)));
    }

    @NotNull
    public Page<Category> findRandom(@NotNull final Pageable pageable) {
        return this.categoryRepository.findRandom(pageable);
    }

    Category getReferenceById(final long id) {
        return this.categoryRepository.getReferenceById(id);
    }

    @Transactional
    boolean increaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.increaseAmountOfProduct(id, amount) > 0;
    }

    @Transactional
    boolean decreaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.decreaseAmountOfProduct(id, amount) > 0;
    }

    private void handleSaveCategory(@NotNull final Category category) {
        categoryRepository.save(category);
    }
}
