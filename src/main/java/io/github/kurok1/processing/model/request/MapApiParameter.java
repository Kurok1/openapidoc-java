package io.github.kurok1.processing.model.request;

import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class MapApiParameter extends ApiParameter {
    
    private final ApiParameter additionParameter;
    
    public MapApiParameter(int index, ParameterPosition in, boolean required, ApiParameter additionParameter) {
        super(index, in, required);
        this.additionParameter = additionParameter;
    }
    
    public ApiParameter getAdditionParameter() {
        return additionParameter;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.OBJECT_MAP;
    }
}
