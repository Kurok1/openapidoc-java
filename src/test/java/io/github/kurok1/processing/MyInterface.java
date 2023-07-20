package io.github.kurok1.processing;

import io.github.kurok1.processing.annotation.Ignored;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public interface MyInterface extends Supplier<String> {

    @RequestMapping("/hello")
    @Ignored
    default void foo2() {

    }

    @GetMapping("getString")
    @Override
    default String get() {
        return null;
    }
}
