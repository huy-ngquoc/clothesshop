package vn.uit.clothesshop.controller.client;

import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.service.CategoryService;
import vn.uit.clothesshop.service.ProductService;
import vn.uit.clothesshop.service.ProductVariantService;
import vn.uit.clothesshop.specification.ProductSpecification;

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
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizeCounts", productVariantService.countProductVariantBySize());
        model.addAttribute("colorCounts", productVariantService.countProductVariantByColor());
        return "client/homepage/show";
    }

    @GetMapping("/shop")
    public String getShopPage(
            @RequestParam(required = false) @Nullable final String q,
            @RequestParam(required = false) @Nullable final Integer fromPrice,
            @RequestParam(required = false) @Nullable final Integer toPrice,
            @RequestParam(required = false) @Nullable Set<@NotBlank String> colors,
            @RequestParam(required = false) @Nullable Set<@NotBlank String> sizes,
            @PageableDefault(size = 10) final Pageable pageable,
            final Model model) {
        final var safePaging = this.sanitizePagable(pageable);

        final var spec = ProductSpecification
                .nameLike(q)
                .and(ProductSpecification.priceBetween(fromPrice, toPrice))
                .and(ProductSpecification.anyColors(colors))
                .and(ProductSpecification.anySizes(sizes));

        final var result = this.productService.handleFindAllProduct(spec, safePaging);

        model.addAttribute("products", result.getContent());
        model.addAttribute("currentPage", result.getNumber());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("sizeCounts", productVariantService.countProductVariantBySize());
        model.addAttribute("colorCounts", productVariantService.countProductVariantByColor());

        return "client/homepage/shop";
    }

    private static final Pageable sanitizePagable(@NotNull final Pageable pageable) {
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
}
