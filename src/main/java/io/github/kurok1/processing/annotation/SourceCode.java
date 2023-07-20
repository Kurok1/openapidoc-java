package io.github.kurok1.processing.annotation;

import java.lang.annotation.*;

/**
 * 请求资源分组
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface SourceCode {

    String[] value() default { "default-project" };

}
