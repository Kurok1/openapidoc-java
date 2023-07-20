package io.github.kurok1.processing;

import io.github.kurok1.processing.annotation.Ignored;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@RestController
public class SimpleController {

    @PostMapping("simple")
    @Ignored
    public void fun() {

    }

}
