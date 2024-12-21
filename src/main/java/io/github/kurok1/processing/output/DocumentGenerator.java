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
