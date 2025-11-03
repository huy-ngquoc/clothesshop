package vn.uit.clothesshop.site.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import vn.uit.clothesshop.cart.dto.request.CartDetailRequest;
import vn.uit.clothesshop.category.service.CategoryService;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.specification.ProductSpecification;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductDetailInfoResponseDto;
import vn.uit.clothesshop.product.presentation.viewmodel.ProductVariantBasicInfoResponseDto;
import vn.uit.clothesshop.product.service.ProductService;
import vn.uit.clothesshop.product.service.ProductVariantService;
import vn.uit.clothesshop.user.domain.User;

@Controller
public class HomepageController {
    private static final Set<String> ALLOWED_SORT = Set.of(
            Product.Fields.id,
            Product.Fields.minPrice,
            Product.Fields.maxPrice,
            Product.Fields.createdAt);

    private final ProductService productService;

    private final ProductVariantService productVariantService;

    private final CategoryService categoryService;

    public HomepageController(
            final ProductService productService,
            final ProductVariantService productVariantService,
            final CategoryService categoryService) {
        this.productService = productService;
        this.productVariantService = productVariantService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String getHomepage(final Model model) {
        final var categoryList = this.categoryService.findRandomForHomepage(3);

        final var productSort = Sort.by(Sort.Order.desc(Product.Fields.createdAt));
        final var productPageRequest = PageRequest.of(0, 4, productSort);
        final var productList = this.productService
                .handleFindAllProductForHomepage(null, productPageRequest)
                .getContent();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("sizeCounts", productVariantService.countProductVariantBySize());
        model.addAttribute("colorCounts", productVariantService.countProductVariantByColor());

        return "client/homepage/show";
    }

    @GetMapping("/shop")
    public String getShopPage(
            @RequestParam(required = false) @Nullable final String q,
            @RequestParam(required = false) @Nullable final Integer fromPrice,
            @RequestParam(required = false) @Nullable final Integer toPrice,
            @RequestParam(required = false) @Nullable Set<@NotNull Long> categoryIds,
            @RequestParam(required = false) @Nullable Set<@NotBlank String> colors,
            @RequestParam(required = false) @Nullable Set<@NotBlank String> sizes,
            @PageableDefault(size = 10) final Pageable pageable,
            final Model model) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user ;
                if(auth==null||auth instanceof AnonymousAuthenticationToken) {
                    user=null;
                } 
                else {
                    user=(User) auth.getPrincipal();
                }
                model.addAttribute("User",user);
        final var safePageable = this.sanitizePageable(pageable);

        final var spec = ProductSpecification
                .nameLike(q)
                .and(ProductSpecification.priceBetween(fromPrice, toPrice))
                .and(ProductSpecification.anyCategoryIds(categoryIds))
                .and(ProductSpecification.anyColors(colors))
                .and(ProductSpecification.anySizes(sizes));

        final var result = this.productService.findAllProduct(spec, safePageable);

        model.addAttribute("products", result.getContent());
        model.addAttribute("currentPage", result.getNumber());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("sizeCounts", productVariantService.countProductVariantBySize());
        model.addAttribute("colorCounts", productVariantService.countProductVariantByColor());

        return "client/homepage/shop";
    }

    private static final Pageable sanitizePageable(@NotNull final Pageable pageable) {
        var mapped = Sort.by(
                pageable.getSort().stream().map((final var order) -> {
                    final var property = order.getProperty();

                    if (!ALLOWED_SORT.contains(property)) {
                        return null;
                    }

                    return new Sort.Order(order.getDirection(), property);
                }).filter(Objects::nonNull).toList());

        if (mapped.isUnsorted()) {
            mapped = Sort.by(Sort.Order.asc(Product.Fields.id));
        } else if (mapped.stream().noneMatch(o -> o.getProperty().equals(Product.Fields.id))) {
            mapped = mapped.and(Sort.by(Sort.Order.asc(Product.Fields.id)));
        } else {
            // Sort is ready!
        }

        final var pageSize = Math.clamp(pageable.getPageSize(), 1, 100);

        return PageRequest.of(pageable.getPageNumber(), pageSize, mapped);
    }

    @GetMapping("/product/detail/{id}")
    public String getProductDetail(final Model model, @PathVariable long id) {
        ProductDetailInfoResponseDto responseDto = productService.handleFindProductById(id);
        model.addAttribute("responseDto", responseDto);
        Set<String> sizeSet = new HashSet<>();
        Set<String> colorSet = new HashSet<>();
        for (ProductVariantBasicInfoResponseDto pvBasicInfo : responseDto.getVariantList()) {
            sizeSet.add(pvBasicInfo.getSize());
            colorSet.add(pvBasicInfo.getColor());
        }
        CartDetailRequest request = new CartDetailRequest(0,0);
        model.addAttribute("cart_request", request);
        model.addAttribute("sizeList", new ArrayList<>(sizeSet));
        model.addAttribute("colorList", new ArrayList<>(colorSet));
        return "client/homepage/productdetail";
    }
}
