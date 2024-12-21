package io.github.kurok1.processing.model.request;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ValueApiParameter extends ApiParameter {
    
    private final ValueType valueType;
    
    public ValueApiParameter(int index, ParameterPosition in, boolean required, ValueType valueType) {
        super(index, in, required);
        this.valueType = valueType;
    }
    
    @Override
    public ValueType getType() {
        return valueType;
    }
}
