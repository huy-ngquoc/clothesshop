package vn.uit.clothesshop.area.admin.product.controller;

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
import jakarta.validation.Valid;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminCreationForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminImageUpdateForm;
import vn.uit.clothesshop.area.admin.product.presentation.form.ProductVariantAdminInfoUpdateForm;
import vn.uit.clothesshop.area.admin.product.service.ProductVariantAdminService;
import vn.uit.clothesshop.area.shared.constant.ModelAttributeConstant;

@Controller
@RequestMapping("/admin/product-variant")
public class ProductVariantAdminController {
    private final ProductVariantAdminService productVariantService;

    public ProductVariantAdminController(
            ProductVariantAdminService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @GetMapping("/{id}")
    public String getProductVariantDetail(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = productVariantService.findDetailById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/productvariant/detail";
    }

    @GetMapping("/create")
    public String getAddVariantPage(
            final Model model,
            @RequestParam final long productId) {
        final var viewModelAndForm = this.productVariantService.findCreationByProductId(productId).orElse(null);

        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/productvariant/create";
    }

    @PostMapping("/create")
    public String addVariant(
            final Model model,
            @RequestParam final long productId,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final ProductVariantAdminCreationForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var viewModel = this.productVariantService.findCreationViewModelByProductId(productId).orElse(null);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);
            return "admin/productvariant/create";
        }

        final var productVariantId = productVariantService.create(productId, form);
        return "redirect:/admin/product-variant/" + productVariantId;
    }

    @GetMapping("/update/info/{id}")
    public String getUpdateVariantPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.productVariantService.findInfoUpdateById(id).orElse(null);

        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/productvariant/updateinfo";
    }

    @PostMapping("/update/info/{id}")
    public String updateVariantInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final ProductVariantAdminInfoUpdateForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var viewModel = this.productVariantService.findInfoUpdateViewModelById(id).orElse(null);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);
            return "admin/productvariant/updateinfo";
        }

        productVariantService.updateInfoById(id, form);
        return "redirect:/admin/product-variant/" + id;
    }

    @GetMapping("/update/image/{id}")
    public String getUpdateProductVariantImagePage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.productVariantService.findImageUpdateById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }
        return "admin/productvariant/updateimage";
    }

    @PostMapping("/update/image/{id}")
    public String updateProductVariantImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final ProductVariantAdminImageUpdateForm form) {
        productVariantService.updateImageById(id, form);
        return "redirect:/admin/product-variant/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getDeleteProductVariantPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.productVariantService.findDeletionViewModelById(id).orElse(null);
        if (viewModel == null) {
            return "redirect:/admin/product";
        }

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/productvariant/delete";

    }

    @PostMapping("/delete/{id}")
    public String deleteProductVariant(
            final Model model,
            @PathVariable final long id) {
        final var productId = this.productVariantService.findProductIdById(id).orElse(null);
        if (productId == null) {
            return "redirect:/admin/product";
        }

        this.productVariantService.deleteById(id);
        return "redirect:/admin/product/" + productId;
    }

}
