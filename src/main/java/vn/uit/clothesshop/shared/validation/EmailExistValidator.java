package vn.uit.clothesshop.shared.validation;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.uit.clothesshop.feature.user.infra.jpa.repository.UserRepository;

@Component
public class EmailExistValidator implements ConstraintValidator<EmailExist, String> {
    private final UserRepository userRepo;

    public EmailExistValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepo.existsByUsername(value);
    }
}
