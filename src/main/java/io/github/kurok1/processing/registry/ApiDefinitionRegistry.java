/*
 * Copyright [2024] [Kurok1]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
