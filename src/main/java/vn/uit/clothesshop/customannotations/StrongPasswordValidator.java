package vn.uit.clothesshop.customannotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       if(value==null) return false;
       if(value.length()<8) return false;
       return true;
    }

}
