package com.example.backend.security.validation;

import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.utils.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserOwnerRequest> {
    @Override
    public boolean isValid(RegisterUserOwnerRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return request.getUserData().getPassword().equals(request.getUserData().getConfirmPassword());
    }
}
