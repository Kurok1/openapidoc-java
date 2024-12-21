package io.github.kurok1.processing.output;

import io.github.kurok1.processing.registry.ApiDefinitionRegistry;
import io.github.kurok1.processing.registry.SchemaRegistry;
import io.github.kurok1.processing.registry.TagRegistry;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public interface DocumentGenerator {
    
    boolean enabled();
    
    Struct write(TagRegistry tagRegistry, SchemaRegistry schemaRegistry, ApiDefinitionRegistry apiDefinitionRegistry);

    String getDocumentPath();
    
    void registerInterceptor(DocumentGeneratorInterceptor lifecycle);
}
