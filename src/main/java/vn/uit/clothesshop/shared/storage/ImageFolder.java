package vn.uit.clothesshop.shared.storage;

public enum ImageFolder {
    CATEGORY("clothesshop_category"),
    PRODUCT("product"),
    PRODUCT_VARIANT("clothesshop"),
    USER("user"),
    ;

    private final String sub;

    private ImageFolder(final String s) {
        this.sub = s;
    }

    public String sub() {
        return sub;
    }
}
