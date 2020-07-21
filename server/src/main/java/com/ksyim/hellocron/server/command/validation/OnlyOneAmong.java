package com.ksyim.hellocron.server.command.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = OnlyOneAmongValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnlyOneAmong {

    String message() default "only one out of {properties} fields can have non-null value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] properties() default {};
}
