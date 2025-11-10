package vn.uit.clothesshop.area.shared.exception.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e) {
        return "error/show";
    }
}
