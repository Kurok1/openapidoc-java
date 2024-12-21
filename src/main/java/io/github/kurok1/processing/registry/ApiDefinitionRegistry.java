package io.github.kurok1.processing.registry;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;
import io.github.kurok1.processing.model.ApiModelDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ApiDefinitionRegistry {
    
    private final List<ApiModelDefinition> definitions = new ArrayList<>();
    
    public ApiDefinitionRegistry() {}
    
    public void addFromBuilder(ApiDefinitionBuilder definition) {
        for (OpenApiContract.PathMethodInfo methodInfo : definition.getPathMethods()) {
            ApiModelDefinition apiModelDefinition = new ApiModelDefinition(definition, methodInfo);
            add(apiModelDefinition);
        }
    }
    
    public void add(ApiModelDefinition definition) {
        definitions.add(definition);
    }
    
    public List<ApiModelDefinition> getDefinitions() {
        return Collections.unmodifiableList(definitions);
    }
    
}
