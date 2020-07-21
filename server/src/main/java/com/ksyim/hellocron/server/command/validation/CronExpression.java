package com.ksyim.hellocron.server.command.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CronExpressionValidator.class })
@Documented
public @interface CronExpression {

    String message() default "not a cron expression";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
