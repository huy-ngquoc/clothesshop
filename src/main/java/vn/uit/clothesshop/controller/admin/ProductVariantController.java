package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.request.ProductVariantCreateRequest;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateImageRequest;
import vn.uit.clothesshop.dto.request.ProductVariantUpdateRequest;
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

    @GetMapping("/update_variant/{variantId}") 
    public String getUpdateVariantPage(final Model model, @PathVariable long variantId) {
        ProductVariant pv = productVariantService.findProductVariantById(variantId);
        ProductVariantUpdateRequest requestDto=null;
        if(pv!=null) {
            requestDto = new ProductVariantUpdateRequest(pv.getId(),pv.getColor(), pv.getSize(),pv.getPriceCents(),pv.getStockQuantity() , pv.getWeightGrams());
            model.addAttribute("image","productvariant/"+pv.getImage());
        }
        else {
            model.addAttribute("image",null);
        }
        model.addAttribute("requestDto",requestDto);
        
        return "admin/productvariant/updateinfo";
    }
    @PostMapping("/update_variant")
    public String updateVariantInfo(final Model model, @Valid @ModelAttribute("requestDto") ProductVariantUpdateRequest requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ProductVariant pv = productVariantService.findProductVariantById(requestDto.getProductVariantId());
            
            if(pv!=null) {
            requestDto = new ProductVariantUpdateRequest(pv.getId(),pv.getColor(), pv.getSize(),pv.getPriceCents(),pv.getStockQuantity() , pv.getWeightGrams());
            model.addAttribute("image","productvariant/"+pv.getImage());
            }
            else {
                model.addAttribute("image",null);
            }
            model.addAttribute("requestDto",requestDto);
            return "admin/productvariant/updateinfo";
        }
        ProductVariant pv = productVariantService.updateInfo(requestDto);
        return "redirect:/admin/product/"+pv.getProductId();
    }

    @GetMapping("/update_variant/image/{variantId}")
    public String getUpdateProductVariantImagePage(final Model model, @PathVariable long variantId){
        ProductVariant pv = productVariantService.findProductVariantById(variantId);
        ProductVariantUpdateImageRequest requestDto=null;
        if(pv!=null) {
            requestDto = new ProductVariantUpdateImageRequest();
            requestDto.setProductVariantId(pv.getId());
            model.addAttribute("image","productvariant/"+pv.getImage());
            }
            else {
                model.addAttribute("image",null);
            }
            model.addAttribute("requestDto",requestDto);
        return "admin/productvariant/updateimage";
    }
    @PostMapping("/update_variant/image/{variantId}")
    public String updateProductVariantImage(final Model model, @PathVariable long variantId,@ModelAttribute("imageFile") final MultipartFile imageFile )
    {
        ProductVariant pv = productVariantService.updateProductVariantImage(imageFile, variantId);
        if(pv==null) {
            return "redirect:/admin/product";
        } 
        return "redirect:/admin/product/"+pv.getProductId();
    }

    @GetMapping("/{variantId}")
    public String getProductVariantDetail(final Model model, @PathVariable long variantId) {
        ProductVariant pv = productVariantService.findProductVariantById(variantId);
        model.addAttribute("variant",pv);
        return "admin/productvariant/detail";
    }

    
}
