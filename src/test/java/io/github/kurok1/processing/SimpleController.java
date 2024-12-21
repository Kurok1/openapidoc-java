package io.github.kurok1.processing;

import io.github.kurok1.processing.model.Result;
import io.github.kurok1.processing.model.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@RestController
public class SimpleController {
    
    public void fun() {

    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/fun")
    public void fun(Result<String> result) {
    
    }
    
    @PostMapping("result1")
    public Result<String> fun2() {
        return null;
    }
    
    @PostMapping("result2")
    public Result<String> fun3(@RequestBody(required = false) Result<User> result) {
        return null;
    }
    
    @PutMapping("result3")
    public Result<String> fun4(@RequestParam(required = false) String name, @RequestParam int value) {
        return null;
    }
    
    @DeleteMapping("delete")
    public List<User> fun5() {
        return null;
    }
    
    @DeleteMapping("delete2")
    public List<Map<String, User>> fun6() {
        return null;
    }

}
