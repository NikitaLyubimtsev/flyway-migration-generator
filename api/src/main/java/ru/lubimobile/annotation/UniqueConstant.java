package ru.lubimobile.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.CLASS)
public @interface UniqueConstant {

    String name() default "";

    String[] columnNames();
}
