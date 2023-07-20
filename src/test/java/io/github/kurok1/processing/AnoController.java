package io.github.kurok1.processing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping("ano")
public class AnoController implements ParamInterface<String> {
    public String contribute() {
        return null;
    }

    @Override
    public String get() {
        return ParamInterface.super.get();
    }
}
