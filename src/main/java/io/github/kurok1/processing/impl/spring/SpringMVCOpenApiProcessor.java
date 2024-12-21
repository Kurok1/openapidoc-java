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

package io.github.kurok1.processing.impl.spring;

import io.github.kurok1.processing.AbstractOpenApiProcessor;
import io.github.kurok1.processing.AnnotationUtils;
import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.builder.AbstractApiDefinitionContributor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@SupportedAnnotationTypes(value = {SpringAnnotations.ANNOTATION_CONTROLLER, SpringAnnotations.ANNOTATION_REST_CONTROLLER})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SpringMVCOpenApiProcessor extends AbstractOpenApiProcessor {
    
    
    @Override
    protected boolean elementIsEligible(Element element) {
        String usingControllerAnnotation = AnnotationUtils.determineAnnotationAnyPresent(element, SpringAnnotations.ANNOTATION_REST_CONTROLLER, SpringAnnotations.ANNOTATION_CONTROLLER);
        return !usingControllerAnnotation.isEmpty();
    }
    
    @Override
    protected OpenApiContract createOpenApiContract() {
        return new SpringMVCContract();
    }
    
    @Override
    protected List<AbstractApiDefinitionContributor> loadAdditionalContributors(OpenApiContract contract) {
        return Collections.emptyList();
    }
}
