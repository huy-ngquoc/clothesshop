package vn.uit.clothesshop.customannotations;

import java.util.Objects;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatches, Object> {
    private String passwordField;
    private String confirmPasswordField;
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordField= constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object password= new BeanWrapperImpl(value).getPropertyValue(passwordField);
            Object confirmPassword = new BeanWrapperImpl(value).getPropertyValue(confirmPasswordField);
            if(password==null||confirmPassword==null) {
                return false;
            } 
            boolean result= password.equals(confirmPassword);
            if(!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match")
                   .addPropertyNode("confirmPassword")
                   .addConstraintViolation();
            }
            return result;
        }
        catch(Exception e) {
            return false;
        }
    }

}
