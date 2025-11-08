package vn.uit.clothesshop.product.controller;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.domain.specification.ProductSpecification;
import vn.uit.clothesshop.product.domain.specification.ProductVariantSpecification;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductInfoUpdateForm;
import vn.uit.clothesshop.product.service.ProductService;
import vn.uit.clothesshop.shared.constant.ModelAttributeConstant;
import vn.uit.clothesshop.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.shared.util.PageableSanitizer;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    private static final Set<String> ALLOWED_PRODUCT_SORT = Set.of(
            Product.Fields.id,
            Product.Fields.name);

    private static final Set<String> ALLOWED_PRODUCT_VARIANT_SORT = Set.of(
            ProductVariant.Fields.id,
            ProductVariant.Fields.color,
            ProductVariant.Fields.size,
            ProductVariant.Fields.stockQuantity,
            ProductVariant.Fields.weightGrams,
            ProductVariant.Fields.priceCents,
            ProductVariant.Fields.sold);

    private static final PageableSanitizer productPageableSanitizer;
    private static final PageableSanitizer productVariantPageableSanitizer;

    private final ProductService productService;

    static {
        productPageableSanitizer = new PageableSanitizer(
                ALLOWED_PRODUCT_SORT,
                Product.Fields.id);

        productVariantPageableSanitizer = new PageableSanitizer(
                ALLOWED_PRODUCT_VARIANT_SORT,
                ProductVariant.Fields.id);
    }

    public ProductController(
            final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProductPage(
            final Model model,
            @RequestParam(required = false) @Nullable final String q,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var safePageable = ProductController.productPageableSanitizer.sanitize(pageable);

        final var spec = ProductSpecification.nameLike(q)
                .or(ProductSpecification.shortDescLike(q))
                .or(ProductSpecification.detailDescLike(q));

        final var page = this.productService.findAllBasic(spec, safePageable);
        model.addAttribute(ModelAttributeConstant.PAGE, page);
        return "admin/product/show";
    }

    @GetMapping("/{id}")
    public String getProductDetailPage(
            final Model model,
            @PathVariable final long id,
            @RequestParam(required = false) @Nullable final String q,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var safePageable = ProductController.productVariantPageableSanitizer.sanitize(pageable);
        final var spec = ProductVariantSpecification.colorLike(q)
                .or(ProductVariantSpecification.sizeLike(q));

        final var viewModel = this.productService.findDetailById(id, spec, safePageable).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/product/detail";
    }

    @GetMapping("/create")
    public String getProductCreationPage(
            final Model model,
            @RequestParam(required = false) @Nullable final Integer categoryPage,
            @RequestParam(required = false) @Nullable final Integer categorySize,
            final HttpServletRequest request) {
        final var categoryPaging = ProductController.createPageableForCategory(categoryPage, categorySize);

        final var viewModelAndForm = this.productService.findCreation(categoryPaging);
        final var viewModel = viewModelAndForm.getFirst();
        final var form = viewModelAndForm.getSecond();

        final var binder = new ServletRequestDataBinder(form, ModelAttributeConstant.FORM);
        binder.bind(request);

        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);
        model.addAttribute(ModelAttributeConstant.FORM, form);

        return "admin/product/create";
    }

    @PostMapping("/create")
    public String createProduct(
            final Model model,
            @RequestParam(required = false) @Nullable final Integer categoryPage,
            @RequestParam(required = false) @Nullable final Integer categorySize,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final ProductCreationForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var categoryPaging = ProductController.createPageableForCategory(categoryPage, categorySize);
            final var viewModel = this.productService.findCreationViewModel(categoryPaging);

            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

            return "admin/product/create";
        }

        this.productService.create(form);
        return "redirect:/admin/product";
    }

    @GetMapping("/update/{id}")
    public String getProductUpdatePage(
            final Model model,
            @PathVariable final long id,
            @RequestParam(required = false) @Nullable final Integer categoryPage,
            @RequestParam(required = false) @Nullable final Integer categorySize) {
        final var categoryPaging = ProductController.createPageableForCategory(categoryPage, categorySize);
        final var viewModelAndForm = this.productService.findInfoUpdateById(id, categoryPaging).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/product/update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            final Model model,
            @PathVariable final long id,
            @RequestParam(required = false) @Nullable final Integer categoryPage,
            @RequestParam(required = false) @Nullable final Integer categorySize,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid ProductInfoUpdateForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var categoryPaging = ProductController.createPageableForCategory(categoryPage, categorySize);
            final var viewModel = this.productService.findInfoUpdateViewModelById(id, categoryPaging);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);
            // TODO: keep the param
            return "admin/product/update";
        }

        this.productService.updateInfoById(id, form);
        return "redirect:/admin/product";
    }

    @GetMapping("/delete/{id}")
    public String getProductDeletePage(
            final Model model,
            @PathVariable final long id) {
        model.addAttribute(ModelAttributeConstant.ID, id);
        return "admin/product/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(
            final Model model,
            @PathVariable final long id) {
        this.productService.deleteById(id);
        return "redirect:/admin/product";
    }

    @NonNull
    private static Pageable createPageableForCategory(
            @Nullable final Integer pageNumber,
            @Nullable final Integer size) {
        final int actualPageNumber = (pageNumber != null) ? pageNumber : 0;
        final int actualSize = (size != null)
                ? Math.clamp(size, PagingConstraint.MIN_SIZE, PagingConstraint.MAX_SIZE)
                : PagingConstraint.DEFAULT_SIZE;

        final var mapped = Sort.by(Sort.Order.asc(Category.Fields.name));
        return PageRequest.of(actualPageNumber, actualSize, mapped);
    }
}