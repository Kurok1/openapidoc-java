package io.github.kurok1.processing.annotation;

import java.lang.annotation.*;

/**
 * 参数别名
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Alias {

    String value();

}
