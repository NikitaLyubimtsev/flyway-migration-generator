package ru.lubimobile.annotation;

import ru.lubimobile.enums.GenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface GeneratedValue {

    GenerationType strategy() default ru.lubimobile.enums.GenerationType.AUTO;

    String generator() default "";
}
