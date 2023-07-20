package io.github.kurok1.processing;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface ParamInterface<V> extends Supplier<V> {

    @GetMapping("param")
    @Override
    default V get() {
        return null;
    }

}
