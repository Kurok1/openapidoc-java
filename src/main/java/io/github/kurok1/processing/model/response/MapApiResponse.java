package io.github.kurok1.processing.model.response;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class MapApiResponse extends ApiResponse {
    
    private ApiResponse additionProperty;
    
    public MapApiResponse(ResponseStatus status, ApiResponse additionProperty) {
        super(status);
        this.additionProperty = additionProperty;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.OBJECT_MAP;
    }
    
    public ApiResponse getAdditionProperty() {
        return additionProperty;
    }
}
