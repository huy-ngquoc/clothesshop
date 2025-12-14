package vn.uit.clothesshop.area.shared.exception.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException ex, Model model) {
        
        String errorMessage = ex.getMessage();
        
        
        if (errorMessage == null) {
            errorMessage = "Lỗi Null Pointer (Dữ liệu bị rỗng không mong muốn)";
        }

        
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorType", ex.getClass().getSimpleName()); 
        
        
        return "exception/exceptionhandler";
    }
}
