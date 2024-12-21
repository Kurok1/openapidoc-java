package io.github.kurok1.processing.model.response;

import io.github.kurok1.processing.model.Schema;
import io.github.kurok1.processing.model.ValueType;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class SchemaApiResponse extends ApiResponse {
    
    private final Schema schema;
    
    public SchemaApiResponse(ResponseStatus status, Schema schema) {
        super(status);
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
