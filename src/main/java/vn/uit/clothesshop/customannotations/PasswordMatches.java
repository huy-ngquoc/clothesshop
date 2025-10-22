package vn.uit.clothesshop.customannotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy=PasswordMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordMatches {
    public String passwordField();
    public String confirmPasswordField();
    String message() default "Password and confirm password does not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
