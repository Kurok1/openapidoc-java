package io.github.kurok1.processing.model.response;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ValueApiResponse extends ApiResponse {
    
    private final ValueType valueType;
    
    public ValueApiResponse(ResponseStatus status, ValueType valueType) {
        super(status);
        this.valueType = valueType;
    }
    
    @Override
    public ValueType getType() {
        return valueType;
    }
    
    
}
