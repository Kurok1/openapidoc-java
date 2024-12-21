package io.github.kurok1.processing.output;

import io.github.kurok1.processing.model.SchemaDefinition;
import io.github.kurok1.processing.model.Tag;
import io.github.kurok1.processing.model.ApiModelDefinition;
import io.github.kurok1.processing.registry.ApiDefinitionRegistry;
import io.github.kurok1.processing.registry.SchemaRegistry;
import io.github.kurok1.processing.registry.TagRegistry;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public interface DocumentGeneratorInterceptor {
    
    void onTagCollect(TagRegistry registry, Tag tag);
    
    void onApiCollect(ApiDefinitionRegistry registry, ApiModelDefinition apiModel);
    
    void onSchemaCollect(SchemaRegistry schemaRegistry, SchemaDefinition schema);
    
}
