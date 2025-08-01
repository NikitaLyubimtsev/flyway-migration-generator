package ru.lubimobile.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Column {

    String name() default "";

    boolean unique() default false;

    boolean nullable() default true;

    String defaultValue() default "";

    int length() default 0;

    int precision() default 0;

    int scale() default 0;
}
