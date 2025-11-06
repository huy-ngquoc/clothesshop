package vn.uit.clothesshop.shared.constraint;

public final class PagingConstraint {
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 100;
    public static final int DEFAULT_SIZE = 10;

    static {
        assert (MIN_SIZE <= DEFAULT_SIZE) && (DEFAULT_SIZE <= MAX_SIZE);
    }

    private PagingConstraint() {
    }
}
