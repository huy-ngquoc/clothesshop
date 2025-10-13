package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequestDto;
import vn.uit.clothesshop.service.ProductService;
import vn.uit.clothesshop.service.ProductVariantService;

@Controller
@RequestMapping("/admin/product/{productId}/variant")
public class ProductVariantController {
    private final ProductService productService;
    private final ProductVariantService productVariantService;

    public ProductVariantController(ProductService productService, ProductVariantService productVariantService) {
        this.productService = productService;
        this.productVariantService = productVariantService;
    }

    @GetMapping("/create")
    public String getAddVariantPage(
            final Model model,
            @PathVariable long productId) {
        final var requestDto = this.buildCreateRequestDto(productId);

        model.addAttribute("productId", productId);
        model.addAttribute("requestDto", requestDto);

        // TODO: fix this view
        return "admin/product/createVariant";
    }

    @PostMapping("/add_variant")
    public String addVariant(final Model model,
            @PathVariable long productId,
            @Valid @ModelAttribute("requestDto") final ProductVariantCreateRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Product p = productService.findProductById(productId);
            model.addAttribute("product", p);
            model.addAttribute("productId", productId);
            return "admin/productvariant/create";
        }
        productVariantService.createProductVariant(productId, requestDto);
        return "redirect:/admin/product/" + productId;
    }

    @Nullable
    private ProductVariantCreateRequestDto buildCreateRequestDto(final long productId) {
        if (!this.productService.existsProductById(productId)) {
            return null;
        }

        return new ProductVariantCreateRequestDto();
    }

}
