package io.github.kurok1.processing;

import io.github.kurok1.processing.annotation.SourceCode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping(MyController.name)
@SourceCode("mySource")
public class MyController extends MyOperations implements MyInterface {

    public static final String name = "my";

    @DeleteMapping("delete")
    public void delete() {

    }

    @Override
    public void foo2() {
        MyInterface.super.foo2();
    }

    @GetMapping("/get")
    @SourceCode("app")
    public void load() {

    }

    @Override
    public void foo() {
        super.foo();
    }

    public void foo(Integer name) {
        super.foo();
    }
}
