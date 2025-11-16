package vn.uit.clothesshop.area.shared.exception.advice;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(String message) {
        super(message);
    }
}
