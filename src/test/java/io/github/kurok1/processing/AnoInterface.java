package io.github.kurok1.processing;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface AnoInterface extends ParamInterface<String> {

    @Override
    default String get() {
        return contribute();
    }

    String contribute();
}
