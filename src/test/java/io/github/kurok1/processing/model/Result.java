package io.github.kurok1.processing.model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class Result<T> {
    
    private int code;
    
    private String message;
    
    public T data;
    
    private Map<String, String> headers;
    
    public List<Long> ids;
    
    private List<T> results;
    
    private User usedUser;
    
}
