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

package io.github.kurok1.processing.builder;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class MethodPathInfoContributor extends AbstractApiDefinitionContributor {
    
    public MethodPathInfoContributor(OpenApiContract openApiContract) {
        super(openApiContract);
    }
    
    @Override
    public void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement) {
        List<OpenApiContract.PathMethodInfo> methodInfoList = super.getOpenApiContract().resolvePathInfo(classElement, methodElement);
        methodInfoList.forEach(definition::addPathMethodInfo);
    }
}
