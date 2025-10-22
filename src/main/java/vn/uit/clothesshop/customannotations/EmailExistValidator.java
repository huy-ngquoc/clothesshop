package vn.uit.clothesshop.customannotations;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.uit.clothesshop.domain.entity.User;
import vn.uit.clothesshop.repository.UserRepository;

@Component
public class EmailExistValidator implements ConstraintValidator<EmailExist, String>  {
    private final UserRepository userRepo;
    public EmailExistValidator(UserRepository userRepo) {
        this.userRepo= userRepo;
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User user = userRepo.findByEmail(value);
        return user!=null;
    }
}
