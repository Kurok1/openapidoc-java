package io.github.kurok1.processing.model.request;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ArrayApiParameter extends ApiParameter {
    
    private final ApiParameter component;
    
    public ArrayApiParameter(int index, ParameterPosition in, boolean required, ApiParameter component) {
        super(index, in, required);
        this.component = component;
    }
    
    public ApiParameter getComponent() {
        return component;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }
}
