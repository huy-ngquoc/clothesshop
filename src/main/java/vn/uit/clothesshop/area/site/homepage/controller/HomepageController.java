package vn.uit.clothesshop.area.site.homepage.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import vn.uit.clothesshop.area.admin.category.service.CategoryAdminService;
import vn.uit.clothesshop.area.admin.product.service.ProductAdminService;
import vn.uit.clothesshop.area.admin.product.service.ProductVariantAdminService;
import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.area.site.cart.presentation.request.CartRequest;
import vn.uit.clothesshop.area.site.homepage.service.ProductClientService;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.spec.ProductSpecification;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.infra.jpa.repository.UserRepository;
import vn.uit.clothesshop.shared.util.PageableSanitizer;

@Controller
public class HomepageController {
    private static final Set<String> ALLOWED_SORT = Set.of(
            Product.Fields.id,
            Product.Fields.minPrice,
            Product.Fields.maxPrice,
            Product.Fields.createdAt);

    private static final PageableSanitizer pageableSanitizer;

    private final ProductAdminService productService;
    private final ProductVariantAdminService productVariantService;
    private final CategoryAdminService categoryService;
    private final UserRepository userRepo;
    private final ProductClientService productClientService;

    static {
        pageableSanitizer = new PageableSanitizer(ALLOWED_SORT, Product.Fields.id);
    }

    public HomepageController(
            final ProductAdminService productService,
            final ProductVariantAdminService productVariantService,
            final CategoryAdminService categoryService, final UserRepository userRepo,
            final ProductClientService productClientService) {
        this.productService = productService;
        this.productVariantService = productVariantService;
        this.categoryService = categoryService;
        this.userRepo = userRepo;
        this.productClientService = productClientService;
    }

    @GetMapping("/")
    public String getHomepage(final Model model) {
        final var categoryList = this.categoryService.findRandomForHomepage(3);

        final var productSort = Sort.by(Sort.Order.desc(Product.Fields.createdAt));
        final var productPageRequest = PageRequest.of(0, 4, productSort);
        final var productList = this.productClientService.findAllBasic(null, productPageRequest);

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
            @PageableDefault(size = 10) @NonNull final Pageable pageable,
            final Model model, Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }
        String username = auth.getName();
        User user = userRepo.findByEmail(username).orElse(null);
        model.addAttribute("user", user);
        final var safePageable = pageableSanitizer.sanitize(pageable);

        final var spec = ProductSpecification.nameLike(q)
                .and(ProductSpecification.priceBetween(fromPrice, toPrice))
                .and(ProductSpecification.anyCategoryIds(categoryIds))
                .and(ProductSpecification.anyColors(colors))
                .and(ProductSpecification.anySizes(sizes));

        final var result = this.productClientService.findAllBasic(spec, safePageable);

        model.addAttribute("products", result);
        model.addAttribute("currentPage", result.getNumber());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("sizeCounts", productVariantService.countProductVariantBySize());
        model.addAttribute("colorCounts", productVariantService.countProductVariantByColor());

        return "client/homepage/shop";
    }

    @GetMapping("/product/detail/{id}")
    public String getProductDetail(
            final Model model,
            @PathVariable long id,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        Product responseDto = productService.findById(id).orElse(null);
        model.addAttribute("responseDto", responseDto);
        Set<String> sizeSet = new HashSet<>();
        Set<String> colorSet = new HashSet<>();
        for (ProductVariant pvBasicInfo : responseDto.getVariants()) {
            sizeSet.add(pvBasicInfo.getSize());
            colorSet.add(pvBasicInfo.getColor());
        }
        CartRequest request = new CartRequest(0, 0);
        model.addAttribute("cart_request", request);
        model.addAttribute("sizeList", new ArrayList<>(sizeSet));
        model.addAttribute("colorList", new ArrayList<>(colorSet));
        return "client/homepage/productdetail";
    }
}
