package vn.uit.clothesshop.product.controller;

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
import vn.uit.clothesshop.product.presentation.form.ProductVariantCreationForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateImageForm;
import vn.uit.clothesshop.product.presentation.form.ProductVariantUpdateInfoForm;
import vn.uit.clothesshop.product.service.ProductVariantService;

@Controller
@RequestMapping("/admin/product-variant")
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    public ProductVariantController(
            ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @GetMapping("/{id}")
    public String getProductVariantDetail(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = productVariantService.findDetailById(id).orElse(null);

        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);

        return "admin/productvariant/detail";
    }

    @GetMapping("/create")
    public String getAddVariantPage(
            final Model model,
            @RequestParam long productId) {
        final var viewModel = this.productVariantService.getCreationInfo(productId).orElse(null);

        model.addAttribute("productId", productId);

        if (viewModel != null) {
            model.addAttribute("requestDto", viewModel.getForm());
        }

        return "admin/productvariant/create";
    }

    @PostMapping("/create")
    public String addVariant(
            final Model model,
            @RequestParam long productId,
            @ModelAttribute("requestDto") @Valid final ProductVariantCreationForm requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productvariant/create";
        }

        final var productVariantId = productVariantService.create(productId, requestDto);
        return "redirect:/admin/product-variant/" + productVariantId;
    }

    @GetMapping("/update/info/{id}")
    public String getUpdateVariantPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.productVariantService.getUpdateInfoById(id).orElse(null);

        if (viewModel != null) {
            model.addAttribute("productId", viewModel.getProductId());
            model.addAttribute("image", viewModel.getImageFilePath());
            model.addAttribute("requestDto", viewModel.getForm());
        }

        return "admin/productvariant/updateinfo";
    }

    @PostMapping("/update/info/{id}")
    public String updateVariantInfo(
            final Model model,
            @PathVariable final long id,
            @Valid @ModelAttribute("requestDto") ProductVariantUpdateInfoForm requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productvariant/updateinfo";
        }

        productVariantService.updateInfoById(id, requestDto);
        return "redirect:/admin/product-variant/" + id;
    }

    @GetMapping("/update/image/{id}")
    public String getUpdateProductVariantImagePage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.productVariantService.getUpdateInfoById(id).orElse(null);

        if (viewModel != null) {
            model.addAttribute("productId", viewModel.getProductId());
            model.addAttribute("image", viewModel.getImageFilePath());
            model.addAttribute("requestDto", viewModel.getForm());
        }
        return "admin/productvariant/updateimage";
    }

    @PostMapping("/update/image/{id}")
    public String updateProductVariantImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final ProductVariantUpdateImageForm requestDto) {
        productVariantService.updateImageById(id, requestDto);
        return "redirect:/admin/product-variant/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getDeleteProductVariantPage(final Model model,
            @PathVariable final long id) {
        if (!this.productVariantService.existsById(id)) {
            return "redirect:/admin/product";
        }
        model.addAttribute("id", id);
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
