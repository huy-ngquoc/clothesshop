package vn.uit.clothesshop.shared.port.storage;

public enum ImageFolder {
    CATEGORY("category"),
    PRODUCT("product"),
    ;

    private final String sub;

    ImageFolder(String s) {
        this.sub = s;
    }

    public String sub() {
        return sub;
    }
}
