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
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequest;
import vn.uit.clothesshop.service.ProductService;
import vn.uit.clothesshop.service.ProductVariantService;



@Controller
@RequestMapping("/admin/product_variant")
public class ProductVariantController {
    private final ProductService productService;
    private final ProductVariantService productVariantService;
    public ProductVariantController(ProductService productService, ProductVariantService productVariantService) {
        this.productService= productService;
        this.productVariantService=productVariantService;
    }
    @GetMapping("/add_variant/{productId}")
    public String getAddVariantPage(final Model model, @PathVariable long productId) {
        ProductVariantCreateRequest requestDto = new ProductVariantCreateRequest();
        Product p = productService.findProductById(productId);
        requestDto.setProductId(productId);
        model.addAttribute("product",p);
        model.addAttribute("requestDto",requestDto);
        model.addAttribute("productId", productId);
       
        return "admin/productvariant/create";
    }
    @PostMapping("/add_variant")
    public String addVariant(final Model model,@Valid @ModelAttribute("requestDto") final ProductVariantCreateRequest requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Product p = productService.findProductById(requestDto.getProductId());
        model.addAttribute("product", p);
        model.addAttribute("productId", requestDto.getProductId());
            return "admin/productvariant/create";
        }
        ProductVariant result = productVariantService.createProductVariant(requestDto);
        return "redirect:/admin/product/"+requestDto.getProductId();
    }
    
    
}
