package ru.lubimobile.annotation;

import java.lang.annotation.*;

@Repeatable(SequenceGenerators.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface SequenceGenerator {

    String name();

    String sequenceName() default "";

    String catalog() default "";

    String schema() default "";

    int initialValue() default 1;

    int allocationSize() default 50;
}
