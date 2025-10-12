package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.dto.request.ProductCreationRequestDto;
import vn.uit.clothesshop.dto.request.ProductUpdateRequestDto;
import vn.uit.clothesshop.service.ProductService;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @NotNull
    private final ProductService productService;

    public ProductController(
            @NotNull final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String getProductDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.productService.handleFindProductById(id);

        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);

        return "";
    }

    @GetMapping("/create")
    public String getProductCreationPage(
            final Model model) {
        final var requestDto = new ProductCreationRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "";
    }

    @PostMapping("/create")
    public String createProduct(
            final Model model,
            @ModelAttribute("requestDto") final ProductCreationRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "";
        }

        this.productService.handleCreateProduct(requestDto);
        return "";
    }

    @GetMapping("/update/{id}")
    private String getProductUpdatePage(
            final Model model,
            @PathVariable final long id) {
        final var requestDto = new ProductUpdateRequestDto();

        model.addAttribute("id", id);
        model.addAttribute("requestDto", requestDto);

        return "";
    }

    @PostMapping("/update/{id}")
    private String updateProduct(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final ProductUpdateRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "";
        }

        this.productService.handleUpdateProduct(id, requestDto);
        return "";
    }

    @GetMapping("/delete/{id}")
    private String getProductDeletePage(
            final Model model,
            @PathVariable final long id) {
        model.addAttribute("id", id);
        return "";
    }

    @PostMapping("/delete/{id}")
    private String deleteProduct(
            final Model model,
            @PathVariable final long id) {
        this.productService.deleteProductById(id);
        return "";
    }
}
