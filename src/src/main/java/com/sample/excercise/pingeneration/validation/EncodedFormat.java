package com.sample.excercise.pingeneration.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EncodingAndFormatValidator.class)
public @interface EncodedFormat {
    String message() default "{format.error.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
