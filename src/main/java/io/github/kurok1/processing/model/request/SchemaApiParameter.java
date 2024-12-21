package io.github.kurok1.processing.model.request;

import io.github.kurok1.processing.model.Schema;
import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class SchemaApiParameter extends ApiParameter {
    
    private final Schema schema;

    public SchemaApiParameter(int index, ParameterPosition in, boolean required, Schema schema) {
        super(index, in, required);
        this.schema = schema;
    }
    
    public Schema getSchema() {
        return schema;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }
}
