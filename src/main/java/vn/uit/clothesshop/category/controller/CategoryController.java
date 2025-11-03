package vn.uit.clothesshop.category.controller;

import org.springframework.data.domain.PageRequest;
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
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateImageForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.service.CategoryService;

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
        final var responseDtoList = this.categoryService
                .findAllBasic(PageRequest.of(0, 100)).toList();
        model.addAttribute("responseDtoList", responseDtoList);
        return "admin/category/show";
    }

    @GetMapping("/{id}")
    public String getCategoryDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.categoryService.findDetailById(id);
        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);
        return "admin/category/detail";
    }

    @GetMapping("/create")
    public String getCategoryCreationPage(
            final Model model) {
        final var requestDto = new CategoryCreationForm();
        model.addAttribute("requestDto", requestDto);
        return "admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            final Model model,
            @ModelAttribute("requestDto") @Valid final CategoryCreationForm requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/create";
        }

        this.categoryService.create(requestDto);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/info/{id}")
    public String getCategoryUpdateInfoPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.categoryService.getUpdateInfoById(id).orElse(null);

        model.addAttribute("id", id);
        if (viewModel != null) {
            model.addAttribute("requestDto", viewModel.getForm());
            model.addAttribute("imageFilePath", viewModel.getImageFilePath());
        }

        return "admin/category/update/info";
    }

    @PostMapping("/update/info/{id}")
    public String updateCategoryInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") @Valid CategoryUpdateInfoForm requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/update/info";
        }

        this.categoryService.updateInfoById(id, requestDto);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/image/{id}")
    public String getCategoryUpdateImagePage(
            final Model model,
            @PathVariable final long id) {
        final var imageFilePath = this.categoryService.getPathStringById(id).orElse(null);

        model.addAttribute("id", id);
        model.addAttribute("imageFilePath", imageFilePath);
        model.addAttribute("requestDto", new CategoryUpdateImageForm());

        return "admin/category/update/image";
    }

    @PostMapping("/update/image/{id}")
    public String updateCategoryImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final CategoryUpdateImageForm requestDto) {
        this.categoryService.updateImageById(id, requestDto.getImageFile());
        return "redirect:/admin/category/update/info/" + id;
    }

    @GetMapping("/update/image/delete/{id}")
    public String getCategoryUpdateAvatarDeletionPage(
            final Model model,
            @PathVariable final long id) {
        final var imageFilePath = this.categoryService.getPathStringById(id).orElse(null);

        model.addAttribute("id", id);
        model.addAttribute("imageFilePath", imageFilePath);

        return "admin/category/update/imageDeletion";
    }

    @PostMapping("/update/image/delete/{id}")
    public String updateCategoryImageDeletion(
            final Model model,
            @PathVariable final long id) {
        this.categoryService.deleteImageById(id);
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
        this.categoryService.deleteById(id);
        return "redirect:/admin/category";
    }
}
