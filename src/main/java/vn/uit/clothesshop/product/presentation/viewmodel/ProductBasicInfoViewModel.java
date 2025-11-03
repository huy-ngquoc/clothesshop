package vn.uit.clothesshop.product.presentation.viewmodel;

public final class ProductBasicInfoViewModel {
    private final long id;
    private final String name;
    private final String shortDesc;

    public ProductBasicInfoViewModel(
            final long id,
            final String name,
            final String shortDesc) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }
}
