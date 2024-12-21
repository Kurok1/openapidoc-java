package io.github.kurok1.processing.model.response;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ArrayApiResponse extends ApiResponse {
    
    private final ApiResponse component;
    
    public ArrayApiResponse(ResponseStatus status, ApiResponse component) {
        super(status);
        this.component = component;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }
    
    public ApiResponse getComponent() {
        return component;
    }
}
