package vn.uit.clothesshop.category.controller;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.domain.specification.CategorySpecification;
import vn.uit.clothesshop.category.presentation.form.CategoryCreationForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateImageForm;
import vn.uit.clothesshop.category.presentation.form.CategoryUpdateInfoForm;
import vn.uit.clothesshop.category.service.CategoryService;
import vn.uit.clothesshop.shared.constant.ModelAttributeConstant;
import vn.uit.clothesshop.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.shared.util.PageableSanitizer;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    private static final Set<String> ALLOWED_SORT = Set.of(
            Category.Fields.id,
            Category.Fields.name,
            Category.Fields.desc);

    private static final PageableSanitizer pageableSanitizer;

    @NotNull
    private final CategoryService categoryService;

    static {
        pageableSanitizer = new PageableSanitizer(ALLOWED_SORT, Category.Fields.id);
    }

    public CategoryController(
            @NotNull CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCategoryPage(
            final Model model,
            @RequestParam(required = false) @Nullable final String q,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var safePageable = CategoryController.pageableSanitizer.sanitize(pageable);
        final var spec = CategorySpecification.nameLike(q)
                .or(CategorySpecification.descLike(q));

        final var page = this.categoryService.findAllBasic(spec, safePageable);

        model.addAttribute(ModelAttributeConstant.PAGE, page);
        return "admin/category/show";
    }

    @GetMapping("/{id}")
    public String getCategoryDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.categoryService.findDetailById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/category/detail";
    }

    @GetMapping("/create")
    public String getCategoryCreationPage(
            final Model model) {
        final var form = new CategoryCreationForm();
        model.addAttribute(ModelAttributeConstant.FORM, form);
        return "admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            final Model model,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid final CategoryCreationForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/create";
        }

        this.categoryService.create(form);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/info/{id}")
    public String getCategoryUpdateInfoPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.categoryService.findInfoUpdateById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/category/update/info";
    }

    @PostMapping("/update/info/{id}")
    public String updateCategoryInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid CategoryUpdateInfoForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var viewModel = this.categoryService.findInfoUpdateViewModelById(id).orElse(null);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);
            return "admin/category/update/info";
        }

        this.categoryService.updateInfoById(id, form);
        return "redirect:/admin/category";
    }

    @GetMapping("/update/image/{id}")
    public String getCategoryUpdateImagePage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.categoryService.findImageUpdateById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
        model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());

        return "admin/category/update/image";
    }

    @PostMapping("/update/image/{id}")
    public String updateCategoryImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) final CategoryUpdateImageForm form) {
        this.categoryService.updateImageById(id, form);

        return "redirect:/admin/category/update/info/" + id;
    }

    @GetMapping("/update/image/delete/{id}")
    public String getCategoryUpdateAvatarDeletionPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.categoryService.findImageDeleteViewModelById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

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
        model.addAttribute(ModelAttributeConstant.ID, id);
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
