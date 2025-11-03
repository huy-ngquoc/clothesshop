package vn.uit.clothesshop.product.controller;

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
import vn.uit.clothesshop.category.domain.CategoryAccess;
import vn.uit.clothesshop.product.presentation.form.ProductCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductUpdateInfoForm;
import vn.uit.clothesshop.product.service.ProductService;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @NotNull
    private final ProductService productService;
    private final CategoryAccess categoryAccess;

    public ProductController(
            @NotNull final ProductService productService, CategoryAccess categoryAccess) {
        this.productService = productService;
        this.categoryAccess = categoryAccess;
    }

    @GetMapping
    public String getProductPage(final Model model) {
        // TODO: for now
        final var responseDtoList = this.productService.findAllBasic(PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        model.addAttribute("responseDtoList", responseDtoList);
        return "admin/product/show";
    }

    @GetMapping("/{id}")
    public String getProductDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.productService.findDetailById(id);
        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);
        return "admin/product/detail";
    }

    @GetMapping("/create")
    public String getProductCreationPage(
            final Model model) {
        final var requestDto = new ProductCreationForm();
        model.addAttribute("requestDto", requestDto);
        // TODO: for now
        model.addAttribute("categories", categoryAccess.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        return "admin/product/create";
    }

    @PostMapping("/create")
    public String createProduct(
            final Model model,
            @ModelAttribute("requestDto") @Valid final ProductCreationForm requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/product/create";
        }

        this.productService.create(requestDto);
        return "redirect:/admin/product";
    }

    @GetMapping("/update/{id}")
    public String getProductUpdatePage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.productService.getUpdateInfoById(id).orElse(null);

        model.addAttribute("id", id);
        model.addAttribute("requestDto", viewModel.getForm());
        // TODO: for now
        model.addAttribute("categories", categoryAccess.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent());

        return "admin/product/update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") @Valid ProductUpdateInfoForm requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getObjectName());
            return "admin/product/update";
        }

        this.productService.updateInfoById(id, requestDto);
        return "redirect:/admin/product";
    }

    @GetMapping("/delete/{id}")
    public String getProductDeletePage(
            final Model model,
            @PathVariable final long id) {
        model.addAttribute("id", id);
        return "admin/product/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(
            final Model model,
            @PathVariable final long id) {
        this.productService.deleteById(id);
        return "redirect:/admin/product";
    }

}