package vn.uit.clothesshop.controller.admin;

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
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequestDto;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateImageRequestDto;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateRequestDto;
import vn.uit.clothesshop.service.ProductVariantService;

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
        final var responseDto = productVariantService.handleFindProductVarianById(id);

        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);

        return "admin/productvariant/detail";
    }

    @GetMapping("/create")
    public String getAddVariantPage(
            final Model model,
            @RequestParam long productId) {
        final var requestDto = this.productVariantService.handleCreateRequestDto(productId);

        model.addAttribute("productId", productId);
        model.addAttribute("requestDto", requestDto);

        // TODO: fix this view
        return "admin/productvariant/create";
    }

    @PostMapping("/create")
    public String addVariant(
            final Model model,
            @RequestParam long productId,
            @ModelAttribute("requestDto") @Valid final ProductVariantCreateRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productvariant/create";
        }

        // TODO: what if null due to cannot save?
        final var productVariantId = productVariantService.createProductVariant(productId, requestDto);
        return "redirect:/admin/product-variant/" + productVariantId;
    }

    @GetMapping("/update/info/{id}")
    public String getUpdateVariantPage(
            final Model model,
            @PathVariable final long id) {
        final var middleDto = this.productVariantService.handleCreateMiddleDtoForUpdateInfo(id);

        if (middleDto != null) {
            model.addAttribute("productId", middleDto.getProductId());
            model.addAttribute("image", middleDto.getAvatarFilePath());
            model.addAttribute("requestDto", middleDto.getRequestDto());
        }

        return "admin/productvariant/updateinfo";
    }

    @PostMapping("/update/info/{id}")
    public String updateVariantInfo(
            final Model model,
            @PathVariable final long id,
            @Valid @ModelAttribute("requestDto") ProductVariantUpdateRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productvariant/updateinfo";
        }

        final var productVariant = productVariantService.updateInfo(id, requestDto);
        return "redirect:/admin/product/" + productVariant.getProductId();
    }

    @GetMapping("/update/image/{id}")
    public String getUpdateProductVariantImagePage(
            final Model model,
            @PathVariable final long id) {
        final var middleDto = this.productVariantService.handleCreateMiddleDtoForUpdateImage(id);

        if (middleDto != null) {
            model.addAttribute("productId", middleDto.getProductId());
            model.addAttribute("image", middleDto.getImageFilePath());
            model.addAttribute("requestDto", middleDto.getRequestDto());
        }
        return "admin/productvariant/updateimage";
    }

    @PostMapping("/update/image/{id}")
    public String updateProductVariantImage(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final ProductVariantUpdateImageRequestDto requestDto) {
        final var succeed = productVariantService.updateProductVariantImage(requestDto, id);
        if (!succeed) {
            return "redirect:/admin/product";
        }

        return "redirect:/admin/product-variant/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getDeleteProductVariantPage(final Model model,
            @PathVariable final long id) {
        if (!this.productVariantService.existsProductVariantById(id)) {
            return "redirect:/admin/product";
        }
        model.addAttribute("id", id);
        return "admin/productvariant/delete";

    }

    @PostMapping("/delete/{id}")
    public String deleteProductVariant(
            final Model model,
            @PathVariable final long id) {
        final var productVariant = this.productVariantService.findProductVariantById(id);
        if (productVariant == null) {
            return "redirect:/admin/product";
        }

        final var productId = productVariant.getProductId();
        this.productVariantService.deleteProductVariant(productVariant);

        return "redirect:/admin/product/" + productId;
    }

}
