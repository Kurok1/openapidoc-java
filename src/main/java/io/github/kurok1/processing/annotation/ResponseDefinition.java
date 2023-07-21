package io.github.kurok1.processing.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
@Repeatable(ResponseDefinitions.class)
public @interface ResponseDefinition {

    String status();

    String description();

}
