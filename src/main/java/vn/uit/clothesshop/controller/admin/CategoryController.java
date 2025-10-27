package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.dto.request.CategoryCreationRequestDto;
import vn.uit.clothesshop.dto.request.CategoryUpdateImageRequestDto;
import vn.uit.clothesshop.dto.request.CategoryUpdateInfoRequestDto;
import vn.uit.clothesshop.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @NotNull
    private final CategoryService categoryService;

    public CategoryController(
            @NotNull CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCategoryPage(final Model model) {
        final var responseDtoList = this.categoryService.handleFindAll();
        model.addAttribute("responseDtoList", responseDtoList);
        return "admin/category/show";
    }

    @GetMapping("/{id}")
    public String getCategoryDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.categoryService.handleFindById(id);
        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);
        return "admin/category/detail";
    }

    @GetMapping("/create")
    public String getCategoryCreationPage(
            final Model model) {
        final var requestDto = new CategoryCreationRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            final Model model,
            @ModelAttribute("requestDto") @Valid final CategoryCreationRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/create";
        }

        this.categoryService.handleCreateCategory(requestDto);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/info/{id}")
    public String getCategoryUpdateInfoPage(
            final Model model,
            @PathVariable final long id) {
        final var middleDto = this.categoryService.handleCreateMiddleDtoForUpdate(id);

        model.addAttribute("id", id);
        if (middleDto != null) {
            model.addAttribute("requestDto", middleDto.requestDto());
            model.addAttribute("imageFilePath", middleDto.imageFilePath());
        }

        return "admin/category/update/info";
    }

    @PostMapping("/update/info/{id}")
    public String updateCategoryInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") @Valid CategoryUpdateInfoRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/update/info";
        }

        this.categoryService.handleUpdateCategory(id, requestDto);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/image/{id}")
    public String getCategoryUpdateImagePage(
            final Model model,
            @PathVariable final long id) {
        final var imageFilePath = this.categoryService.findImageFilePathOfCategoryById(id);

        model.addAttribute("id", id);
        model.addAttribute("imageFilePath", imageFilePath);
        model.addAttribute("requestDto", new CategoryUpdateImageRequestDto());

        return "admin/category/update/image";
    }

    @PostMapping("/update/image/{id}")
    public String updateCategoryImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final CategoryUpdateImageRequestDto requestDto) {
        this.categoryService.handleUpdateCategoryImage(id, requestDto.getImageFile());
        return "redirect:/admin/category/update/info/" + id;
    }

    @GetMapping("/update/image/delete/{id}")
    public String getCategoryUpdateAvatarDeletionPage(
            final Model model,
            @PathVariable final long id) {
        final var imageFilePath = this.categoryService.findImageFilePathOfCategoryById(id);

        model.addAttribute("id", id);
        model.addAttribute("imageFilePath", imageFilePath);

        return "admin/category/update/imageDeletion";
    }

    @PostMapping("/update/image/delete/{id}")
    public String updateCategoryImageDeletion(
            final Model model,
            @PathVariable final long id) {
        this.categoryService.handleUpdateCategoryImageDeletion(id);
        return "redirect:/admin/category/update/info/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getCategoryDeletePage(
            final Model model,
            @PathVariable final long id) {
        model.addAttribute("id", id);
        return "admin/category/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            final Model model,
            @PathVariable final long id) {
        this.categoryService.deleteCategoryById(id);
        return "redirect:/admin/category";
    }
}
