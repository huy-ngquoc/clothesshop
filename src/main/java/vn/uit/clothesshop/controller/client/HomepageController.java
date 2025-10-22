package vn.uit.clothesshop.controller.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.dto.request.FilterRequest;
import vn.uit.clothesshop.dto.response.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductVariantBasicInfoResponseDto;
import vn.uit.clothesshop.dto.response.ProductVariantDetailInfoResponseDto;
import vn.uit.clothesshop.service.HomePageService;
import vn.uit.clothesshop.service.ProductService;
import vn.uit.clothesshop.utils.ParamValidator;

@Controller
public class HomepageController {
    private final HomePageService homePageService;
    private final ProductService productService;
    public HomepageController(HomePageService homePageService, ProductService productService) {
        this.homePageService = homePageService;
        this.productService = productService;
        
    }
    @GetMapping("/")
    public String getHomepage(final Model model) {
        model.addAttribute("categories",homePageService.getAllCategories());
        model.addAttribute("sizeCounts", homePageService.getSizeCounts());
        model.addAttribute("colorCounts", homePageService.getColorCounts());
        return "client/homepage/show";
    }

    @GetMapping("/shop")
    public String getShopPage(final Model model, @RequestParam("page") Optional<String> pageOptional, @RequestParam("name") Optional<String> nameOptional, @RequestParam("fromPrice") Optional<String> fromPrice,
    @RequestParam("toPrice") Optional<String> toPrice, @RequestParam(value="colors", required=false) List<String> colors, @RequestParam(value="sizes", required = false) List<String> sizes) {
        String pageString = pageOptional.orElse("1");
        int pageNumber=1;
        String name = nameOptional.orElse(null);
        try {
            pageNumber = Integer.parseInt(pageString);
        }
        catch(Exception e) {
            return "client/homepage/show";
        }
        FilterRequest request = new FilterRequest(pageNumber, name, ParamValidator.getIntFromOptional(fromPrice), ParamValidator.getIntFromOptional(toPrice), colors, sizes);

        Page<Product> pageProducts= homePageService.filterProduct(request);
        List<Product> listProducts = pageProducts.getContent();
        model.addAttribute("products",listProducts);
        model.addAttribute("currentPage",pageProducts.getNumber());
        model.addAttribute("totalPages",pageProducts.getTotalPages());
        model.addAttribute("sizeCounts", homePageService.getSizeCounts());
        model.addAttribute("colorCounts", homePageService.getColorCounts());
        return "client/homepage/shop";
    }

    @GetMapping("/product/detail/{id}") 
    public String getProductDetail(final Model model, @PathVariable long id) {
        ProductDetailInfoResponseDto responseDto = productService.handleFindProductById(id);
        model.addAttribute("responseDto", responseDto);
        Set<String> sizeSet = new HashSet<>();
        Set<String> colorSet = new HashSet<>();
        for(ProductVariantBasicInfoResponseDto pvBasicInfo: responseDto.getVariantList()) {
            sizeSet.add(pvBasicInfo.getSize());
            colorSet.add(pvBasicInfo.getColor());
        } 
        model.addAttribute("sizeList",new ArrayList<>(sizeSet));
        model.addAttribute("colorList", new ArrayList<>(colorSet));
        return "client/homepage/productdetail";
    }
}
