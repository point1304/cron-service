package com.ksyim.hellocron.server.command.validation;

import java.lang.reflect.Field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyOneAmongValidator implements ConstraintValidator<OnlyOneAmong, Object> {

    private String[] incompatibleProperties;

    @Override
    public void initialize(OnlyOneAmong constraintAnnotation) {
        incompatibleProperties = constraintAnnotation.properties();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = false;

        Class<?> clazz = value.getClass();
        for (String propertyName : incompatibleProperties) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {}

            try {
                if (field != null && field.get(value) != null) {
                    if (!isValid) { isValid = true; } else { return false; }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return isValid;
    }
}
