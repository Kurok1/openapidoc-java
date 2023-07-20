package io.github.kurok1.processing;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public abstract class MyParent {

    @RequestMapping("/")
    public abstract void foo();

}
