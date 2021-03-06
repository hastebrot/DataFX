package io.datafx.crud.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewColumn {

    String value() default "";

    int index() default -1;

    boolean sortable() default true;

    boolean editable() default true;

    boolean resizeable() default true;
}
